package com.cml.defaultnominator.service.target;

import com.cml.defaultnominator.dao.internal.TargetRepository;
import com.cml.defaultnominator.dto.container.ContainerOut;
import com.cml.defaultnominator.dto.dictionary.fn.CodeNamePair;
import com.cml.defaultnominator.dto.dictionary.fn.FunctionalNumberDictionaryRequest;
import com.cml.defaultnominator.dto.namedobject.IdentityData;
import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.cml.defaultnominator.dto.target.TargetCreate;
import com.cml.defaultnominator.dto.target.TargetOut;
import com.cml.defaultnominator.dto.target.TargetShort;
import com.cml.defaultnominator.dto.target.TargetSuggest;
import com.cml.defaultnominator.entity.internal.container.Container;
import com.cml.defaultnominator.entity.internal.target.CheckedCombination;
import com.cml.defaultnominator.entity.internal.target.Target;
import com.cml.defaultnominator.service.dictionary.functionalnumber.FunctionalNumberDictionaryService;
import com.cml.defaultnominator.service.namedobject.AbstractNamedObjectService;
import com.cml.defaultnominator.service.targetlibrary.TargetLibraryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.cml.defaultnominator.service.namedobject.NamedObjectTypes.TAR;
import static com.cml.defaultnominator.service.namedobject.NamedObjectTypes.TGR;
import static com.cml.defaultnominator.service.targetlibrary.ApprovalStates.AWAITING;
import static com.cml.defaultnominator.service.targetlibrary.ApprovalStates.NOT_CREATED;

@Service
@Slf4j
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
public class TargetService extends AbstractNamedObjectService<Target, TargetRepository> {

    static String DEFAULT_REVISION = "00";
    static int BASE_NESTING_LEVEL = -1;
    static int MAX_NESTING_LEVEL = 3;

    TargetLibraryService targetLibraryService;
    CheckedCombinationService checkedCombinationService;
    FunctionalNumberDictionaryService fnDictionaryService;

    public TargetService(TargetRepository repository,
                         TargetLibraryService targetLibraryService,
                         CheckedCombinationService checkedCombinationService,
                         FunctionalNumberDictionaryService fnDictionaryService) {
        super(repository);
        this.targetLibraryService = targetLibraryService;
        this.checkedCombinationService = checkedCombinationService;
        this.fnDictionaryService = fnDictionaryService;
    }

    @Override
    public boolean create(NamedObjectRequest nor) {

        TargetCreate request = (TargetCreate) nor;

        if (request.containsNull()) {
            throw new IllegalArgumentException("Invalid request data");
        }

        // 'double request' check
        if (checkById(generateIdByObjectIdAndType(request.getId(), request.getType()))) {
            log.error("Target library object with id={} already exist and hasn't been saved", request.getId());
            return false;
        }

        final int pid = getValidHashedPid(request.getParent());

        Target parent = getParentById(pid);
        if (parent == null) {
            parent = createInitialParent(request);
            save(parent);
        }

        if (request.getParent() == null) {
            request.setParent(new IdentityData());
        }

        request.getParent().setId(pid);

        Target target = getTargetWithUpdatedValues(request);
        save(target);

        if (target.getType().equals(TAR.getFullName())) {
            createAndSaveCheckedCombinationForTarget(target);
        }

        return true;
    }

    /**
     * @param nor {@linkplain TargetSuggest}
     * @return {@linkplain TargetOut}
     */
    @Override
    public TargetOut getSuggestedName(NamedObjectRequest nor) {

        TargetSuggest request = (TargetSuggest) nor;

        if (request.containsNull()) {
            throw new IllegalArgumentException("Invalid request data");
        }

        final int pid = getValidHashedPid(request.getParent());
        TargetOut out = new TargetOut();
        TargetShort target = new TargetShort();

        if (request.getType().equalsIgnoreCase(TAR.getFullName())) {
            target.setRevision(DEFAULT_REVISION);
        }

        Target parent = getTarget(pid);

        calculateCheckboxStatus(out, parent);

        if (parent == null) {
            log.info("Parent entity with id={} is absent", pid);

            out.setDictionary(fnDictionaryService.getLevel());
            target.setNumEntity(DEFAULT_NUMBER_OF_ENTITY);
            target.setFn(new HashMap<>());
            out.setTarget(target);

            log.info("Create new suggested name: {}", out);
            return out;
        }

        target.setProductIndex(parent.getProductIndex());
        target.setFn(getInheritedFn(parent));

        String[] codes = target.getFn().values().stream().map(CodeNamePair::getCode).toArray(String[]::new);
        out.setDictionary(fnDictionaryService.getLevel(parent.getProductIndex(), codes));

        target.setNumEntity(getNumberOfEntityByFnHash(
                convertFnMapToHashableFormat(target.getFn()).hashCode()));

        out.setTarget(target);
        log.info("Create new suggested name: {}", out);
        return out;
    }


    @Override
    public int getValidHashedPid(IdentityData request) {
        int pid;
        if (request == null
                || request.getId() == null
                || request.getType() == null) {
            pid = generateIdByObjectIdAndType(DEFAULT_PARENT_ID, DEFAULT_TARGET_PARENT_TYPE);
        } else {
            pid = generateIdByObjectIdAndType(
                    request.getId(),
                    getValidType(request.getType())
            );
        }
        return pid;
    }

    @Override
    public String getValidType(String type) {
        return type == null
                ? DEFAULT_TARGET_PARENT_TYPE
                : type.toLowerCase();
    }

    private String getNumberOfEntityByFnHash(int hash) {
        String current = repository.getEntityNumberOfTargetWithMaxCreationTimeUsingFnHash(hash);
        if (current == null) {
            return DEFAULT_NUMBER_OF_ENTITY;
        }
        int entities = Integer.parseInt(current);
        return formatValueToStringSpecifiedLength(default_entity_number_length, entities + 1);
    }

    /**
     * Выполняется при нажатии на 'TargetLibrary'<br/>
     * Обновляет имена всех targetGroup и target в дереве
     */
    public void updateAllTargetNames() {
        List<Target> targets = repository.getAllByType(TAR.getFullName());
        int count = 0;
        if (targets.isEmpty()) {
            log.info("No update required");
            return;
        }

        for (Target target : targets) {
            CheckedCombination actualCombination =
                    targetLibraryService.getLastCheckedCombinationByTargetId(target.getObjectId());
            if (actualCombination != null && !actualCombination.getState().equals(AWAITING.getState())) {
                if (!actualCombination.equals(target.getCombination())) {
                    // old state - new state
                    String os = target.getCombination().getState();
                    String ns = actualCombination.getState();
                    if (targetLibraryService.renameNeeded(os, ns)) {
                        log.info("Rename needed. From={} to {}", actualCombination, target.getCombination());
                        checkedCombinationService.save(actualCombination);
                        target.setCombination(actualCombination);
                        target.setRevision(increaseRevision(target.getRevision()));
                        save(target);
                        String name = target.getFormattedName();
                        targetLibraryService.updateTargetNameById(target.getObjectId(), name);
                        count++;
                    }
                }
            }
        }
        log.info("Revisions have been updated for {} targets", count);
    }

    /**
     * Выполняется при нажатии на 'Save' в блоке 'Value' при редактировании таргета<br/>
     * Обновляет номер ревизии с учетом смены статуса новой версии на 'not created'
     *
     * @param objectId non-null int
     */
    public void updateTargetName(int objectId) {
        final int id = generateIdByObjectIdAndType(objectId, TAR.getFullName());
        Target target = getTarget(id);

        if (target == null) {
            log.error("Target with objectId={} not exist", objectId);
            return;
        }

        CheckedCombination lc = targetLibraryService.getLastCheckedCombinationByTargetId(target.getObjectId());

        lc.setTarget(target);

        String os = target.getCombination().getState();
        String ns = lc.getState();

        if (targetLibraryService.renameNeeded(os, ns)) {
            log.info("Target's name will be updated after save");
            String newRev = increaseRevision(target.getRevision());
            target.setRevision(newRev);

            String name = target.getFormattedName();
            targetLibraryService.updateTargetNameById(objectId, name);
        }
        if (!target.getCombination().equals(lc)) {
            log.info("Last checked combination has been updated from state '{}' to '{}'",
                    target.getCombination().getState(), lc.getState());
            target.setCombination(lc);
            checkedCombinationService.save(lc);
        }
        save(target);
    }

    private String increaseRevision(String rev) {
        int newRevisionAsInt = Integer.parseInt(rev) + 1;
        return formatValueToStringSpecifiedLength(2, newRevisionAsInt);
    }

    /**
     * Возвращает только наследуемые от родительской сущности уровни FN<br/>
     * Наследуются все уровни, кроме последнего
     *
     * @param parent родительская сущность
     * @return HashMap
     */
    private Map<String, CodeNamePair> getInheritedFn(Target parent) {
        Map<String, CodeNamePair> out = new HashMap<>();

        int parentNestingLevel = parent.getNestingLevel();

        String[] codes = Arrays.stream(parent.collectFn().values().toArray(new String[0]))
                .filter(Objects::nonNull).toArray(String[]::new);

        if (codes.length < 1) return out;

        List<CodeNamePair> path = fnDictionaryService.getDictionaryObjectNameByCode(parent.getProductIndex(), codes);

        if (path.size() > parentNestingLevel) {
            parentNestingLevel++;
        }

        switch (parentNestingLevel) {
            case 1: {
                out.put(FunctionalNumberField.AA, path.get(0));
                break;
            }
            case 2: {
                out.put(FunctionalNumberField.AA, path.get(0));
                out.put(FunctionalNumberField.BB, path.get(1));
                break;
            }
            case 3: {
                out.put(FunctionalNumberField.AA, path.get(0));
                out.put(FunctionalNumberField.BB, path.get(1));
                out.put(FunctionalNumberField.CC, path.get(2));
                break;
            }
        }
        return out;
    }

    public TargetOut getDictionaryWithActualEntityNumber(FunctionalNumberDictionaryRequest request) {
        TargetOut out = new TargetOut();

        TargetShort target = new TargetShort();

        if (request.getProject() == null || request.getProject().trim().length() == 0) {
            target.setNumEntity(DEFAULT_NUMBER_OF_ENTITY);
            out.setTarget(target);
            out.setDictionary(fnDictionaryService.getLevel());
            return out;
        }

        target.setNumEntity(getNumberOfEntityByFnHash(request.getFields().hashCode()));
        out.setTarget(target);

        List<CodeNamePair> dict = fnDictionaryService.getLevel(request.getProject(), request.convertDictionaryLevelsToArray());
        out.setDictionary(dict);

        return out;
    }

    private void saveCheckedCombination(CheckedCombination cc) {
        checkedCombinationService.save(cc);
    }

    private int calculateNestingLevel(int parentHashedId) {
        Target parent = getTarget(parentHashedId);
        int parentNestingLevel = parent.getNestingLevel();
        int parentFnSize = (int) parent.collectFn().values().stream()
                .filter(value -> !Objects.equals(value, null)).count();

        return parentNestingLevel >= parentFnSize
                ? parentFnSize
                : ++parentNestingLevel;
    }

    private Target getTarget(int hashedId) {
        return getObjectById(hashedId);
    }

    private Target getParentById(int pid) {
        return repository.getTargetByIdAndTypeEqualsTargetgroup(pid).orElse(null);
    }

    private void createAndSaveCheckedCombinationForTarget(Target target) {
        CheckedCombination lc = CheckedCombination.builder()
                .version(DEFAULT_VERSION)
                .state(NOT_CREATED.getState())
                .target(target)
                .build();
        lc.setTarget(target);
        saveCheckedCombination(lc);
        target.setCombination(lc);
        save(target);
    }

    private Target getTargetWithUpdatedValues(TargetCreate request) {
        Target out = new Target();
        int pid = request.getParent().getId();

        out.setObjectId(request.getId());
        out.setType(getValidType(request.getType()));
        out.setId(generateIdByObjectIdAndType(out.getObjectId(), out.getType()));
        out.setProductIndex(request.getProductIndex());
        if (request.getName() == null) {
            out.setName(request.getName());
        } else {
            out.setName(request.getName().trim());
        }
        out.setPid(pid);

        if (out.getType().equals(TAR.getFullName())) {
            out.setRevision(DEFAULT_REVISION);
        } else if (out.getType().equals(TGR.getFullName())) {
            out.setNestingLevel(calculateNestingLevel(pid));
        }

        out.extractAndSetFnFields(request.getFn());
        out.setFnHash(getFnWithoutNullValues(request.getFn()).hashCode());

        out.setNumEntity(getNumberOfEntityByFnHash(out.getFnHash()));
        out.setCreationTime(new Date().getTime());
        log.info("All values was updated: {}", out);
        return out;
    }

    private Target createInitialParent(NamedObjectRequest request) {
        log.info("Parent object in target library is absent and will be created");
        Target parent = new Target();
        if (request.getParent() == null
                || request.getParent().getId() == null
                || request.getParent().getType() == null) {
            parent.setObjectId(DEFAULT_PARENT_ID);
        } else {
            parent.setObjectId(request.getParent().getId());
        }
        parent.setType(DEFAULT_TARGET_PARENT_TYPE);
        parent.setId(generateIdByObjectIdAndType(parent.getObjectId(), parent.getType()));
        parent.setPid(DEFAULT_PARENT_ID);
        parent.setNestingLevel(BASE_NESTING_LEVEL);
        parent.setCreationTime(new Date().getTime());
        return parent;
    }

    /**
     * В случае, если аргумент содержит пары с null-значениями, метод отфильтрует их
     *
     * @param fn Map < String, String > e.g. {'AA': '72', 'BB':'31', 'CC':null, 'DD':null}
     * @return Map < String, String > without 'null' values
     */
    private Map<String, String> getFnWithoutNullValues(Map<String, String> fn) {
        return fn.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue)
                );
    }

    /**
     * Преобразует функциональный номер из формата {"AA":{"71":"Силовая установка"}, "BB":{"10":"Капот"}}
     * в формат {"AA":"72", "BB":"10"}
     *
     * @param in Map< String, CodeNamePair >
     * @return Map<String, String>
     */
    private Map<String, String> convertFnMapToHashableFormat(Map<String, CodeNamePair> in) {
        return in.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getCode())
                );
    }


    private void calculateCheckboxStatus(TargetOut target, Target parent) {
        if (parent == null || parent.isInitial()) {
            target.setClassifierEnabled(false);
            target.setClassifierAvailable(true);
        } else {
            target.setClassifierEnabled(true);
            target.setClassifierAvailable(false);
        }
    }
}
