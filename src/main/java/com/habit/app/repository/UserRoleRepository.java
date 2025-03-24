package com.habit.app.repository;


import com.habit.app.enums.RoleName;
import com.habit.app.model.User;
import com.habit.app.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<User> findByRoleName(RoleName roleName);


    Optional<UserRole> findByUserId(Long UserId);

    boolean existsByUserIdAndRoleName(Long id, RoleName roleName);
}
