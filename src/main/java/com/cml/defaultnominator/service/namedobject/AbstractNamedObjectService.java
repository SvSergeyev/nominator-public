package com.cml.defaultnominator.service.namedobject;

import com.cml.defaultnominator.dao.internal.NamedObjectRepository;
import com.cml.defaultnominator.dto.namedobject.IdentityData;
import com.cml.defaultnominator.entity.internal.namedobject.AbstractNamedObjectEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class AbstractNamedObjectService<E extends AbstractNamedObjectEntity, R extends NamedObjectRepository<E>>
        implements NamedObjectService<E> {

    public final static String DEFAULT_NUMBER_OF_ENTITY = "00001";
    public final static int DEFAULT_VERSION = 1;
    public final static int DEFAULT_PARENT_ID = -1;
    public final static String DEFAULT_CONTAINER_PARENT_TYPE = "folder";
    public final static String DEFAULT_TARGET_PARENT_TYPE = "targetgroup";

    @Value("${services.properties.container.entity-index-length}")
    public int default_entity_number_length;

    protected final R repository;

    public AbstractNamedObjectService(R repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public E save(E object) {
        log.info("Save={}", object);
        return repository.save(object);
    }

    @Override
    public E getObjectById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public boolean checkById(int id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    @Transactional
    public boolean deleteById(int id) {
        repository.deleteById(id);
        if (repository.findById(id).isEmpty()) {
            log.info("Object was successfully deleted");
            return true;
        }
        log.warn("Object was not deleted");
        return false;
    }

    @Override
    public int generateIdByObjectIdAndType(Integer objectId, String type) {
        return new IdentityData(objectId, type).hashCode();
    }

    /**
     * Преобразует {@code value} в строку заданной длины, добавляя нули слева
     *
     * @param length требуемая длина строки
     * @param value  числовое значение, требующее преобразования
     * @return отформатированная строка <i>(например, length = 3, value = 2, return = "002")</i>
     */
    protected static String formatValueToStringSpecifiedLength(int length, int value) {
        int zeroes = length - String.valueOf(value).length();
        return "0".repeat(Math.max(0, zeroes)) + value;
    }
}
