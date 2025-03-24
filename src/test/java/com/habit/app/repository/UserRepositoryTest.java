package com.habit.app.repository;

import com.habit.app.enums.RoleName;
import com.habit.app.model.User;
import com.habit.app.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    private User user;
    private UserRole userRole;
    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeEach
    void init() {
        this.user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("emai@lk.com");

        this.userRole = new UserRole();
        userRole.setRoleName(RoleName.ROLE_USER);
        userRole.setUser(user);

        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        user.setUserRoles(roles);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Save User")
    public void testSaveUser() {
        user = userRepository.save(user);
        assertNotNull(userRepository.findById(user.getId()), "User isn't in db");
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Find User By Username")
    public void testFindUserByUsername() {
        userRepository.save(user);
        userRole.setUser(user);
        userRoleRepository.save(userRole);
        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertNotNull(foundUser, "User is not found by Username");
        assertEquals(foundUser.get(), user);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Update User Password")
    public void testUpdateUserPassword() {
        userRepository.save(user);
        userRole.setUser(user);
        userRoleRepository.save(userRole);
        user.setPassword("newpassword");
        userRepository.save(user);
        Optional<User> updatedUser = userRepository.findByUsername("testuser");
        assertNotNull(updatedUser);
        assertEquals(updatedUser.get().getPassword(), user.getPassword(), "passwords do not match after update");
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Delete User")
    public void testDeleteUser() {
        user = userRepository.save(user);
        userRole.setUser(user);
        userRoleRepository.save(userRole);
        userRepository.delete(user);
        userRoleRepository.delete(userRole);
        Optional<User> deletedUser = userRepository.findByUsername("testuser");
        assertFalse(deletedUser.isPresent(), "Object user is still in db");
    }
}
