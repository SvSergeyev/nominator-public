package com.cml.defaultnominator.entity.internal.namedobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public abstract class AbstractNamedObjectEntity {

    @Id
    @Column(name = "id")
    int id;

    @Column(name = "object_id")
    int objectId;

    @Column(name = "product_index")
    String productIndex;

    @Column(name = "num_entity")
    String numEntity;

    @Column(name = "pid")
    Integer pid;

    @Column(name = "type")
    String type;

    @Column(name = "creation_time")
    long creationTime;
}
