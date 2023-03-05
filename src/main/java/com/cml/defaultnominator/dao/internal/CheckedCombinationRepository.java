package com.cml.defaultnominator.dao.internal;

import com.cml.defaultnominator.entity.internal.target.CheckedCombination;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckedCombinationRepository extends CrudRepository<CheckedCombination, Integer> {
}
