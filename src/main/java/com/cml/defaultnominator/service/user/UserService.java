package com.cml.defaultnominator.service.user;

import com.cml.defaultnominator.dao.remote.core.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    final UserRepository repository;
    private static final String DEFAULT_DEPARTMENT_CODE = "None";

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public String getDepartmentCodeById(int id) {
        if (repository.existsBenchUserById(id)) {
            String departmentCode = repository.getDepartmentCodeUsingId(id);
            return (departmentCode == null || departmentCode.trim().length() < 1)
                    ? DEFAULT_DEPARTMENT_CODE
                    : departmentCode;
        }
        throw new IllegalArgumentException("User with id=" + id + " not exist");
    }
}
