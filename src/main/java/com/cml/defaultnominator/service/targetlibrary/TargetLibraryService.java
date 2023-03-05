package com.cml.defaultnominator.service.targetlibrary;

import com.cml.defaultnominator.entity.internal.target.CheckedCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.cml.defaultnominator.service.targetlibrary.ApprovalStates.*;

/**
 * Обеспечивает взаимодействие между сервисами, работающими с БД 'cml_core_db' и сервисами, работающими с 'cml_tl_db'
 */
@Service
public class TargetLibraryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TargetLibraryService.class);

    private final TargetValueService targetValueService;
    private final BenchTargetService benchTargetService;
    private final ApprovalService approvalService;
    private final ApprovalAssigneeDecisionService approvalAssigneeDecisionService;

    public TargetLibraryService(BenchTargetService benchTargetService,
                                TargetValueService targetValueService,
                                ApprovalService approvalService,
                                ApprovalAssigneeDecisionService approvalAssigneeDecisionService) {
        this.benchTargetService = benchTargetService;
        this.targetValueService = targetValueService;
        this.approvalService = approvalService;
        this.approvalAssigneeDecisionService = approvalAssigneeDecisionService;
    }

    /**
     * Получает последний номер версии и ее статус для таргета
     * @param id идентификатор таргета в таблице cml_tl_db.target
     * @return пара "номер версии" - "статус"
     */
    public CheckedCombination getLastCheckedCombinationByTargetId(int id) {
        Integer version = targetValueService.getLastVersionByTargetObjectId(id);
        // на случай, если произойдет ошибка удаления и сервис будет проверять несуществующий таргет
        if (version == null) {
            return null;
        }
        Integer approval = approvalService.getApprovalIdByTargetIdAndVersion(id, version);
        String state = approvalAssigneeDecisionService.getCalculatedStateByApprovalId(approval);
        return CheckedCombination.builder().version(version).state(state).build();
    }

    /**
     * Делегирует {@linkplain BenchTargetService} переименование таргета
     * @param targetId идентификатор таргета, для которого выполняется обновление имени
     * @param newName новое имя
     */
    public void updateTargetNameById(int targetId, String newName) {
        LOGGER.info("New name={} for target={}", newName, targetId);
        benchTargetService.renameTargetInDatabaseByTargetId(targetId, newName);
    }

    /**
     * Проверяет необходимости смены имении (номера ревизии) таргета, сравнивая состояние
     * предыдущей версии с состоянием последней версии.<br/>
     * Обновление номера ревизии происходит при смене статусов:<br/>
     * os = 'approved' -> ns = 'approved'<br/>
     * os = 'not created' -> ns = 'approved'<br/>
     * os = 'not created' -> ns = 'not created'<br/>
     * os = 'declined' -> ns = 'approved'<br/>
     * os = 'declined' -> ns = 'not created'<br/>
     * os = 'declined' -> ns = 'declined'<br/>
     * @param os (old state)состояние предпоследней версии
     * @param ns (new state) состояние последней версии
     * @return true, если необходимо обновить номер ревизии таргета
     */
    public boolean renameNeeded(String os, String ns) {
        LOGGER.info("Last saved state: {}, actual state: {}", os, ns);
        return (os.equals(APPROVED.getState()) && ns.equals(APPROVED.getState()))
                || (os.equals(NOT_CREATED.getState()) && ns.equals(APPROVED.getState()))
                || (os.equals(NOT_CREATED.getState()) && ns.equals(NOT_CREATED.getState())
                || (os.equals(DECLINED.getState()) && ns.equals(APPROVED.getState()))
                || (os.equals(DECLINED.getState()) && ns.equals(NOT_CREATED.getState()))
                || (os.equals(DECLINED.getState()) && ns.equals(DECLINED.getState())));
    }
}
