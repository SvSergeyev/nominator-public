package com.cml.defaultnominator.dao.internal;

import com.cml.defaultnominator.entity.internal.target.Target;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TargetRepository extends NamedObjectRepository<Target> {

    @Query(value = "select t.numEntity from Target t " +
            "where t.creationTime = (select max(t.creationTime) from Target t where t.fnHash = :hash)")
    String getEntityNumberOfTargetWithMaxCreationTimeUsingFnHash(@Param("hash") int hash);

    List<Target> getAllByType(String type);

    /**
     * Используется для получения объектов, которые могут являться родительской сущностью,
     * то есть только 'TargetGroup'
     * @param id идентификатор объекта
     * @return Optional <'Container'>
     */
    @Query(value = "select t from Target t where t.id = :id and t.type = 'targetgroup'")
    Optional<Target> getTargetByIdAndTypeEqualsTargetgroup(@Param("id") int id);

}
