package com.habit.app.service;

import com.habit.app.dto.TaskDTO;
import com.habit.app.model.Task;
import com.habit.app.repository.TaskRepository;
import com.habit.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public TaskService(UserRepository userRepository, TaskRepository taskRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }


    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasksByUser(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return modelMapper.map(task, TaskDTO.class);
    }

    @Transactional
    public TaskDTO createTask(Long userId, TaskDTO taskDTO) {
        Task task = modelMapper.map(taskDTO, Task.class);
        task.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
        task.setCreatedAt(LocalDateTime.now());
        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDTO.class);
    }
    @Transactional
    public void checkAndCreateRepeatingTasks() {
        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {
            if (task.isRepeating()) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime nextRepeat = task.getDueDate().plusDays(task.getRepeatInterval());

                if (now.isAfter(nextRepeat)) {
                    Task newTask = modelMapper.map(task, Task.class);

                    newTask.setId(null); // Reset ID to ensure a new entity is created
                    newTask.setDueDate(nextRepeat.withHour(task.getRepeatTime().getHour()).withMinute(task.getRepeatTime().getMinute()));
                    newTask.setCreatedAt(LocalDateTime.now());

                    taskRepository.save(newTask);

                    task.setDueDate(nextRepeat);
                    taskRepository.save(task);
                }
            }
        }
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        existingTask.setName(taskDTO.getName());
        existingTask.setDueDate(taskDTO.getDueDate());
        existingTask.setStatus(taskDTO.getStatus());
        existingTask.setPriority(taskDTO.getPriority());
        Task updatedTask = taskRepository.save(existingTask);
        return modelMapper.map(updatedTask, TaskDTO.class);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}
