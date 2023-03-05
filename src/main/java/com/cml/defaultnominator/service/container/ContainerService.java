package com.cml.defaultnominator.service.container;

import com.cml.defaultnominator.dao.internal.ContainerRepository;
import com.cml.defaultnominator.dto.container.ContainerCreate;
import com.cml.defaultnominator.dto.container.ContainerOut;
import com.cml.defaultnominator.dto.container.ContainerSuggest;
import com.cml.defaultnominator.dto.namedobject.ActionType;
import com.cml.defaultnominator.dto.namedobject.IdentityData;
import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.cml.defaultnominator.entity.internal.container.Container;
import com.cml.defaultnominator.service.namedobject.AbstractNamedObjectService;
import com.cml.defaultnominator.service.tree.TreeService;
import com.cml.defaultnominator.service.user.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.cml.defaultnominator.service.namedobject.NamedObjectTypes.formatFullTypeNameToShort;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ContainerService extends AbstractNamedObjectService<Container, ContainerRepository> {

    final UserService userService;
    final TreeService treeService;

    public ContainerService(ContainerRepository repository,
                            UserService userService,
                            TreeService treeService) {
        super(repository);
        this.userService = userService;
        this.treeService = treeService;
    }

    @Override
    public ContainerOut getSuggestedName(NamedObjectRequest nor) {
        ContainerSuggest request = (ContainerSuggest) nor;
        IdentityData parentData = request.getParent();

        // todo create request validator (for null-values and duplicated requests)
        if (request.containsNull()) {
            throw new IllegalArgumentException("Invalid request data: " + request);
        }

        if (parentData == null || parentData.isEmpty()) {
            return new ContainerOut();
        }

        final int hashedPid = getValidHashedPid(request.getParent());

        Container parent = getContainer(hashedPid);
        ContainerOut out = new ContainerOut();

        out.setNumEntity(getCurrentEntityNumber(parentData, ActionType.suggest));
        out.setVersion(getCurrentVersionNumber(hashedPid, request.getType().toLowerCase()));
        out.setType(formatFullTypeNameToShort(request.getType()));

        if (parent == null) {
            calculateCheckboxStatus(out, parentData);
        } else {
            calculateCheckboxStatus(out, parent);
        }

        if (parent == null || parent.isInitial()) {
            out.setDivisionCode(userService.getDepartmentCodeById(request.getUid()));
            return out;
        }

        out.setDivisionCode(parent.getDivisionCode());
        out.setProductIndex(parent.getProductIndex());
        out.setAnalysisType(parent.getAnalysisType());

        log.info("New suggested name: {}", out);
        return out;
    }

    @Override
    @Transactional
    public boolean create(NamedObjectRequest nor) {
        ContainerCreate request = (ContainerCreate) nor;

        // todo create request validator
        if (checkById(generateIdByObjectIdAndType(request.getId(), request.getType()))) {
            log.error("Container with id={} already exist and hasn't been saved", request.getId());
            return false;
        }

        Container parent = getContainer(getValidHashedPid(request.getParent()));

        if (parent == null) {
            parent = getInitialParent(request.getParent());
            save(parent);
        }

        Container container = getObjectWithUpdatedValues(request, parent.isProjectTreeObject(), parent.getRootProjectId());
        save(container);
        return true;
    }


    @Override
    public int getValidHashedPid(IdentityData request) {
        return generateIdByObjectIdAndType(request.getId(), getValidType(request.getType()));
    }


    @Override
    public String getValidType(String type) {
        return type == null ? DEFAULT_CONTAINER_PARENT_TYPE : type.toLowerCase();
    }


    private Container getObjectWithUpdatedValues(ContainerCreate request,
                                                 boolean isProjectBranch,
                                                 Integer rootProjectId) {
        Container out = new Container();

        out.setObjectId(request.getId());
        out.setDivisionCode(request.getDivisionCode());
        out.setProductIndex(request.getProductIndex());
        out.setAnalysisType(request.getAnalysisType());
        out.setType(request.getType().toLowerCase());
        out.setPid(getValidHashedPid(request.getParent()));
        out.setProjectTreeObject(isProjectBranch);
        out.setRootProjectId(rootProjectId);

        out.setId(generateIdByObjectIdAndType(out.getObjectId(), out.getType().toLowerCase()));
        out.setNumEntity(getCurrentEntityNumber(request.getParent(), ActionType.create));
        out.setVersion(getCurrentVersionNumber(out.getPid(), out.getType()));

        out.setCreationTime(new Date().getTime());

        return out;
    }


    private String getCurrentEntityNumber(IdentityData parent, ActionType action) {
        return treeService.isPrivateTree(parent.getId())
                ? getEntityNumberByCurrentLevel(getValidHashedPid(parent))
                : getEntityNumberByCurrentProject(parent, action);
    }


    private String getEntityNumberByCurrentLevel(int pid) {
        String number = repository.getEntityNumberOfEntityWithMaxCreationTimeByPid(pid);
        if (number == null || number.isEmpty()) {
            return DEFAULT_NUMBER_OF_ENTITY;
        }
        int entity = Integer.parseInt(number);
        return formatValueToStringSpecifiedLength(default_entity_number_length, entity + 1);
    }


    private String getEntityNumberByCurrentProject(IdentityData parentData, ActionType action) {
        Container parent = getContainer(getValidHashedPid(parentData));

        if (parent == null) return DEFAULT_NUMBER_OF_ENTITY;

        int projectTreeSize = treeService.countTreeElementsByProjectObjectId(parent.getRootProjectId());
        String nullableMaxEntityNumberFromDb = repository.getEntityNumberWithMaxCreationTimeByRootProject(parent.getRootProjectId());

        if (action == ActionType.create) {
            --projectTreeSize;
        }
        if (nullableMaxEntityNumberFromDb == null) {
            nullableMaxEntityNumberFromDb = "0";
        }

        int dbEntity = Integer.parseInt(nullableMaxEntityNumberFromDb);

        if (projectTreeSize > dbEntity) {
            return formatValueToStringSpecifiedLength(default_entity_number_length, projectTreeSize);
        } else if (projectTreeSize < dbEntity) {
            return formatValueToStringSpecifiedLength(default_entity_number_length, dbEntity);
        } else {
            return formatValueToStringSpecifiedLength(default_entity_number_length, ++dbEntity);
        }
    }


    private Container getInitialParent(IdentityData parent) {
        Container out = new Container();
        boolean isProjectTree = !treeService.isPrivateTree(parent.getId());

        out.setObjectId(parent.getId());
        out.setType(getValidType(parent.getType()));

        if (parent.isProject() && isProjectTree) {
            out.setProjectTreeObject(true);
            out.setRootProjectId(parent.getId());
        }

        out.setId(generateIdByObjectIdAndType(out.getObjectId(), out.getType()));
        out.setPid(DEFAULT_PARENT_ID);
        out.setCreationTime(new Date().getTime());

        return out;
    }


    private String getCurrentVersionNumber(int pid, String type) {
        String ver = repository.getVersionOfContainerWithMaxCreationTimeByPidAndType(pid, type);
        if (ver == null || ver.isEmpty()) {
            return formatValueToStringSpecifiedLength(3, DEFAULT_VERSION);
        }
        int version = Integer.parseInt(ver);
        return formatValueToStringSpecifiedLength(3, version + 1);
    }


    private Container getContainer(int hashedId) {
        return getObjectById(hashedId);
    }

    private void calculateCheckboxStatus(ContainerOut container, IdentityData parent) {
        if (parent == null || parent.isEmpty()) {
            container.setClassifierEnabled(false);
            container.setClassifierAvailable(false);
        } else if (treeService.isPrivateTree(parent.getId())) {
            container.setClassifierEnabled(false);
            container.setClassifierAvailable(true);
        } else if (parent.isProject()) {
            container.setClassifierEnabled(false);
            container.setClassifierAvailable(true);
        } else {
            container.setClassifierEnabled(false);
            container.setClassifierAvailable(false);
        }
    }

    private void calculateCheckboxStatus(ContainerOut container, Container parent) {
        if (parent.isInitial()) {
            container.setClassifierEnabled(false);
            container.setClassifierAvailable(true);
        } else {
            container.setClassifierEnabled(true);
            container.setClassifierAvailable(false);
        }
    }

}
