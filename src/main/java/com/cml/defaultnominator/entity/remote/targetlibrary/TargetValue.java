package com.cml.defaultnominator.entity.remote.targetlibrary;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "target_value")
@Getter
@Setter
public class TargetValue {

    @Id
    @Column(name = "target_id")
    int id;

    @Column(name = "version")
    int version;
}
