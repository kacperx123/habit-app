package com.habit.app.repository;


import com.habit.app.enums.RoleName;
import com.habit.app.model.User;
import com.habit.app.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findUserRoleByRoleName(RoleName roleName);
}
