package com.cml.defaultnominator.entity.remote.core.approval;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "approvals")
public class Approval {
    @Id
    int id;

    @Column(name = "entity_id")
    int target;

    @Column(name = "entity_version_id")
    int version;
}
