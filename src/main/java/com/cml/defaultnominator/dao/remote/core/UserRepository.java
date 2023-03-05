package com.cml.defaultnominator.dao.remote.core;

import com.cml.defaultnominator.entity.remote.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u.departmentCode from User u where u.id = :id")
    String getDepartmentCodeUsingId(@Param("id") int id);

    boolean existsBenchUserById(int id);
}
