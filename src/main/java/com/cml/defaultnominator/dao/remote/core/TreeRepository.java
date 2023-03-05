package com.cml.defaultnominator.dao.remote.core;

import com.cml.defaultnominator.entity.remote.core.tree.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Integer> {

    @Query(value = "select count(*) from tree_join " +
            "where path_ids @> to_jsonb(array(select id from tree_join where object_id = :object_id))",
            nativeQuery = true)
    Integer countAllByPathIdsContainsId(@Param("object_id") int objectId);

    @Query(value = "select is_private from tree_join " +
            "where pid is null and id = cast((select distinct path_ids ->> 0 from tree_join where object_id = :object_id) as int)",
            nativeQuery = true)
    Boolean checkIsPrivateByObjectId(@Param("object_id") int objectId);

    boolean existsByObjectId(int objectId);

}
