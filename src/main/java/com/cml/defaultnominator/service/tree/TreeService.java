package com.cml.defaultnominator.service.tree;

import com.cml.defaultnominator.dao.remote.core.TreeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TreeService {

    private final TreeRepository repository;

    public Integer countTreeElementsByProjectObjectId(int id) {
        Integer count = repository.countAllByPathIdsContainsId(id);
        return count == null ? 1 : count;
    }

    public boolean isPrivateTree(int objectId) {
        if (repository.existsByObjectId(objectId)) {
            return repository.checkIsPrivateByObjectId(objectId);
        } else throw new IllegalArgumentException("Tree node with object id=" + objectId + " not exists");
    }

}
