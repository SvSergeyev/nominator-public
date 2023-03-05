package com.cml.defaultnominator.dao.remote.core;

import com.cml.defaultnominator.entity.remote.core.approval.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Integer> {

    @Query("select a.id from Approval a where a.target = :t_id and a.version = :v_id")
    Integer getApprovalIdByTargetIdAndVersion(@Param("t_id") int targetId,
                                              @Param("v_id") int version);

}
