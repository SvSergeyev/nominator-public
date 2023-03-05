package com.cml.defaultnominator.dao.remote.targetlibrary;

import com.cml.defaultnominator.entity.remote.targetlibrary.BenchTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BenchTargetRepository extends JpaRepository<BenchTarget, Integer> {
    @Query(value = "update target set name = :name where id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    void renameById(@Param("id") int id,
                    @Param("name") String name);
}
