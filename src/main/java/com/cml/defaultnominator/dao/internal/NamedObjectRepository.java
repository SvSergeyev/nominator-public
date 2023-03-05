package com.cml.defaultnominator.dao.internal;

import com.cml.defaultnominator.entity.internal.namedobject.AbstractNamedObjectEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface NamedObjectRepository<E extends AbstractNamedObjectEntity> extends CrudRepository<E, Integer> {

}
