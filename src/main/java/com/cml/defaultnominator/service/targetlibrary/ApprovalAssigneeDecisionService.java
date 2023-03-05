package com.cml.defaultnominator.service.targetlibrary;

import com.cml.defaultnominator.dao.remote.core.ApprovalAssigneeDecisionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Обеспечивает доступ к таблице 'approval_assignees' в БД cml_core_db
 */
@Service
public class ApprovalAssigneeDecisionService {

    private final ApprovalAssigneeDecisionRepository repository;

    public ApprovalAssigneeDecisionService(ApprovalAssigneeDecisionRepository repository) {
        this.repository = repository;
    }

    /**
     * Возвращает актуальный статус аппрува по идентфикатору аппрува
     * @param approvalId идентификатор аппрува в таблице
     * @return текстовое представление {@linkplain ApprovalStates}
     */
    public String getCalculatedStateByApprovalId(Integer approvalId) {
        return calculateState(getAllStatesByApprovalId(approvalId));
    }

    /**
     * Получает список решений, принятых пользователями, указанными как assignee, для аппрува с approval_id=id
     * @param id идентификатор аппрува
     * @return список из текстовых значений колонки 'state'
     */
    private List<String> getAllStatesByApprovalId(Integer id) {
        return repository.getAllStatesUsingApprovalId(id);
    }

    /**
     * Рассчитывает итоговый статус аппрува
     * @param states список всех решений, принятых пользователями, указанными как assignee
     * @return текстовое представление {@linkplain ApprovalStates}
     */
    private String calculateState(List<String> states) {
        if (states == null || states.isEmpty()) return "approval is not created";

        List<String> edited = states.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        if (edited.contains(ApprovalStates.DECLINED.getState())) return ApprovalStates.DECLINED.getState();
        if (edited.contains(ApprovalStates.TIMEOUT.getState())) return ApprovalStates.TIMEOUT.getState();
        if (edited.stream().allMatch(s -> s.equals(ApprovalStates.APPROVED.getState())))
            return ApprovalStates.APPROVED.getState();
        return ApprovalStates.AWAITING.getState();
    }
}
