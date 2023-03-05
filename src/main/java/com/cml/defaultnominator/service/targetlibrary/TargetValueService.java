package com.cml.defaultnominator.service.targetlibrary;

import com.cml.defaultnominator.dao.remote.targetlibrary.TargetValueRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Обеспечивает доступ к таблице 'target_values' в БД 'cml_tl_db'
 */
@Service
public class TargetValueService {
    private final TargetValueRepository repository;

    public TargetValueService(TargetValueRepository repository) {
        this.repository = repository;
    }

    /**
     * Возвращает номер последней созданной версии для таргета по его идентификатору
     * @param id идентификатор таргета, соответствующий значению колонки 'target_id'
     * @return номер последней версии, соответствующий наибольшему значению из колонки 'version'
     * для соответствующего 'target_id'
     */
    public Integer getLastVersionByTargetObjectId(int id) {
        return repository.getLastVersionByTargetObjectId(id);
    }
}
