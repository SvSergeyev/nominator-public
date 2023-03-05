package com.cml.defaultnominator.service.targetlibrary;

import com.cml.defaultnominator.dao.remote.core.ApprovalRepository;
import org.springframework.stereotype.Service;

/**
 * Обеспечивает доступ к таблице 'approvals' в БД cml_core_db
 */
@Service
public class ApprovalService {
    private final ApprovalRepository repository;

    public ApprovalService(ApprovalRepository repository) {
        this.repository = repository;
    }

    /**
     * Получает идентификатор аппрува по комбинации идентификатора таргета и выбранного номера версии таргета
     * @param targetId идентификатор таргета, соответствующий значению колонки 'entity_id'
     *                (равен objectId в дереве 'Target Library' либо 'id' в таблице 'target' БД cml_tl_db)
     * @param version номер версии, соответствующий значению колонки 'entity_version_id'
     * @return nullable идентификатор аппрува. Если к выбранной версии апрув не создавался - вернет null,
     * в противном случае - значение колонки id
     */
    public final Integer getApprovalIdByTargetIdAndVersion(int targetId, int version) {
        return repository.getApprovalIdByTargetIdAndVersion(targetId, version);
    }
}
