package com.cml.defaultnominator.entity.remote.core.tree;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tree_join")
@Immutable
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tree {

    @Id
    @Column(name = "id")
    int id;

    @Column(name = "object_id")
    int objectId;

    @Column(name = "path_ids")
    @ElementCollection
    List<Integer> pathIds;

    @Column(name = "is_private")
    Boolean isPrivate;

    @Column(name = "pid")
    Integer pid;
}
