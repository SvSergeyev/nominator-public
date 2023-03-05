package com.cml.defaultnominator.service.target;

import com.cml.defaultnominator.dao.internal.CheckedCombinationRepository;
import com.cml.defaultnominator.entity.internal.target.CheckedCombination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CheckedCombinationService {
    private final CheckedCombinationRepository repository;

    public CheckedCombinationService(CheckedCombinationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CheckedCombination save(CheckedCombination combination) {
        log.info("Save checked combination={}", combination);
        return repository.save(combination);
    }
}
