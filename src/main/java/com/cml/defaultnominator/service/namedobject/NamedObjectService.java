package com.cml.defaultnominator.service.namedobject;

import com.cml.defaultnominator.dto.namedobject.IdentityData;
import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.cml.defaultnominator.entity.internal.namedobject.AbstractNamedObjectEntity;

public interface NamedObjectService<E extends AbstractNamedObjectEntity> {

    E save(E object);

    E getObjectById(int id);

    boolean checkById(int Id);

    void deleteAll();

    boolean deleteById(int id);

    boolean create(NamedObjectRequest request);

    NamedObjectRequest getSuggestedName(NamedObjectRequest request);

    int getValidHashedPid(IdentityData request);

    String getValidType(String type);

    int generateIdByObjectIdAndType(Integer id, String type);

}
