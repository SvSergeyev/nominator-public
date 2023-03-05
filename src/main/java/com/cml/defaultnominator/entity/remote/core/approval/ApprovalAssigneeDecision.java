package com.cml.defaultnominator.entity.remote.core.approval;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "approval_assignees")
@Getter
@Setter
public class ApprovalAssigneeDecision {

    @Id
    @Column(name = "approval_id")
    int id;

    @Column(name = "state")
    String state;
}
