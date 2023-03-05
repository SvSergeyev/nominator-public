package com.cml.defaultnominator.entity.remote.targetlibrary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "target")
@Getter
@Setter
@ToString
public class BenchTarget {

    @Id
    int id;

    @Column(name = "name")
    String name;
}
