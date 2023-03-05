package com.cml.defaultnominator.service.targetlibrary;

import com.cml.defaultnominator.dao.remote.targetlibrary.BenchTargetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Обеспечивает доступ к таблице 'target' в БД 'cml_tl_db'
 */
@Service
public class BenchTargetService {
    private final BenchTargetRepository repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchTargetService.class);

    public BenchTargetService(BenchTargetRepository repository) {
        this.repository = repository;
    }

    /**
     * Устанавливает новое значение колонки 'name' в таблице 'target' для 'target.id'=id
     * @param id идентификатор таргета в таблице 'target'. Эквивалентен objectId в дереве 'Target Library'
     * @param name новое имя
     */
    void renameTargetInDatabaseByTargetId(int id, String name) {
        LOGGER.info("Target with objectId={} will be renamed to '{}'", id, name);
        repository.renameById(id, name);
    }
}
