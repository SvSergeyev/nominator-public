package com.cml.defaultnominator.dao.remote.targetlibrary;

import com.cml.defaultnominator.entity.remote.targetlibrary.TargetValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetValueRepository extends JpaRepository<TargetValue, Integer> {

    @Query("select max(tv.version) from TargetValue tv where tv.id = :t_object_id")
    Integer getLastVersionByTargetObjectId(@Param("t_object_id") int objectId);
}
