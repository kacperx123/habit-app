package com.habit.app.repository;

import com.habit.app.enums.RoleName;
import com.habit.app.model.User;
import com.habit.app.model.UserRole;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("UserRole")
@DisplayName("Testing UserRole Repository")
public class UserRoleRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    private User user;
    private UserRole userRole;

    @BeforeEach
    void init() {
        this.user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("email@example.com");

        this.userRole = new UserRole();
        userRole.setRoleName(RoleName.ROLE_USER);
        userRole.setUser(user);

        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        user.setUserRoles(roles);

        user = userRepository.save(user);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Save User Role")
    public void testSaveUserRole() {
        userRole = userRoleRepository.save(userRole);
        assertThat(userRole.getId()).withFailMessage("ID of the saved UserRole should not be null").isNotNull();
        assertThat(userRoleRepository.findById(userRole.getId()))
                .withFailMessage("Saved UserRole should be found in the repository").isPresent();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Find User Roles By Role Name")
    public void testFindByRoleName() {
        List<User> users = userRoleRepository.findByRoleName(RoleName.ROLE_USER);
        assertThat(users).withFailMessage("UserRoles list should not be empty").isNotEmpty();
        assertThat(users.get(0).getUsername())
                .withFailMessage("Username of the first UserRole should be 'testuser'").isEqualTo("testuser");
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Update User Role")
    public void testUpdateUserRole() {
        userRole.setRoleName(RoleName.ROLE_ADMIN);
        userRole = userRoleRepository.save(userRole);
        assertThat(userRole.getRoleName()).withFailMessage("UserRole should be updated to ROLE_ADMIN").isEqualTo(RoleName.ROLE_ADMIN);

        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(updatedUser).withFailMessage("Updated User should not be null").isNotNull();
        assertThat(updatedUser.getUserRoles())
                .withFailMessage("Updated User should contain the updated UserRole").contains(userRole);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Delete User Role")
    public void testDeleteUserRole() {
        user.getUserRoles().remove(userRole);
        userRoleRepository.delete(userRole);

        userRepository.save(user);

        assertThat(user.getUserRoles())
                .withFailMessage("User's UserRoles should not contain the deleted UserRole")
                .doesNotContain(userRole);
        assertThat(userRoleRepository.findById(userRole.getId()))
                .withFailMessage("Deleted UserRole should not be found in the repository")
                .isNotPresent();
    }
}
