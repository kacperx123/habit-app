package com.habit.app.repository;
import com.habit.app.enums.RoleName;
import com.habit.app.enums.TaskPriority;
import com.habit.app.model.Task;
import com.habit.app.model.User;
import com.habit.app.model.UserRole;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("Task")
@DisplayName("Testing Task Repository")
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;
    Task task;
    User user;
    TestInfo testInfo;
    TestReporter testReporter;

    @BeforeEach
    @Rollback
    void init(TestInfo testInfo, TestReporter testReporter)
    {
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        testReporter.publishEntry("Starting test " + testInfo.getDisplayName() + " with tags " + testInfo.getTags());

        UserRole userRole = new UserRole();
        userRole.setRoleName(RoleName.ROLE_USER);


        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);


        this.user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("email@com");

        userRole.setUser(user);

        user.setUserRoles(roles);
        user = userRepository.save(user);

        this.task = new Task();
        task.setName("Test Task");
        task.setPriority(TaskPriority.MEDIUM);
        task.setUser(user);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Save Task")
    public void testSaveTask() {
        task = taskRepository.save(task);
        assertThat(task.getId()).isNotNull();
        assertThat(task.getUser()).isNotNull();
        assertThat(task.getUser().getUserRoles()).isNotEmpty();
        assertThat(task.getUser().getUserRoles().iterator().next().getRoleName()).isEqualTo(RoleName.ROLE_USER);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Find Task by ID")
    public void testFindTaskById() {


        task = taskRepository.save(task);

        Task foundTask = taskRepository.findById(task.getId()).orElse(null);
        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getName()).isEqualTo("Test Task");
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Update Task")
    public void testUpdateTask() {;

        task = taskRepository.save(task);

        task.setName("Updated Task");
        taskRepository.save(task);

        Task updatedTask = taskRepository.findById(task.getId()).orElse(null);
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getName()).isEqualTo("Updated Task");
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Test - Delete Task")
    public void testDeleteTask() {
        task = taskRepository.save(task);
        Long taskId = task.getId();

        taskRepository.deleteById(taskId);

        Task deletedTask = taskRepository.findById(taskId).orElse(null);
        assertThat(deletedTask).isNull();
    }
}

