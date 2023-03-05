package com.cml.defaultnominator.dao.internal;

import com.cml.defaultnominator.entity.internal.container.Container;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContainerRepository extends NamedObjectRepository<Container> {

    @Query(value = "select c.numEntity from Container c " +
            "where c.creationTime = (select max(c.creationTime) from Container c where c.pid = :pid)")
    String getEntityNumberOfEntityWithMaxCreationTimeByPid(@Param("pid") int pid);

    @Query(value = "select c.numEntity from Container c where c.creationTime = " +
            "(select max(c.creationTime) from Container c where c.rootProjectId = :root_project_id)")
    String getEntityNumberWithMaxCreationTimeByRootProject(@Param("root_project_id") int projectId);

    @Query(value = "select c.version from Container c " +
            "where c.creationTime = (select max(c.creationTime) from Container c where c.pid = :pid and c.type = :type)")
    String getVersionOfContainerWithMaxCreationTimeByPidAndType(@Param("pid") int pid,
                                                                @Param("type") String type);

}
