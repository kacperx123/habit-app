package com.habit.app.service;

import com.habit.app.dto.TaskDTO;
import com.habit.app.enums.TaskPriority;
import com.habit.app.model.Task;
import com.habit.app.model.User;
import com.habit.app.repository.TaskRepository;
import com.habit.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskDTO taskDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setUser(user);
        task.setPriority(TaskPriority.HIGH);

        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setName("Test Task");
        taskDTO.setPriority(TaskPriority.HIGH);

    }

    @Test
    @DisplayName("Test - Get All Tasks By User")
    void testGetAllTasksByUser() {
        when(taskRepository.findByUserId(1L)).thenReturn(List.of(task));
        when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(taskDTO);

        List<TaskDTO> tasks = taskService.getAllTasksByUser(1L);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getName()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Test - Get Task By Id")
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO foundTask = taskService.getTaskById(1L);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getName()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test - Get Task By Id - Task Not Found")
    void testGetTaskById_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.getTaskById(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Task not found");
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test - Create Task")
    void testCreateTask() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(any(TaskDTO.class), eq(Task.class))).thenReturn(task);
        when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO createdTask = taskService.createTask(1L, taskDTO);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getName()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test - Update Task")
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO updatedTask = taskService.updateTask(1L, taskDTO);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getName()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Test - Update Task - Task Not Found")
    void testUpdateTask_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(1L, taskDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("Task not found");
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test - Delete Task")
    void testDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("Test - Delete Task - Task Not Found")
    void testDeleteTask_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Task not found");
        verify(taskRepository, times(1)).findById(1L);
    }
}
   
