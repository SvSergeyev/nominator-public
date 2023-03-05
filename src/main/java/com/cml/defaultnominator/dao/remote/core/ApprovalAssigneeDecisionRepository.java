package com.cml.defaultnominator.dao.remote.core;

import com.cml.defaultnominator.entity.remote.core.approval.ApprovalAssigneeDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalAssigneeDecisionRepository extends JpaRepository<ApprovalAssigneeDecision, Integer> {

    @Query("select a_s.state from ApprovalAssigneeDecision a_s where a_s.id = :approval_id")
    List<String> getAllStatesUsingApprovalId(@Param("approval_id") Integer approvalId);

}
