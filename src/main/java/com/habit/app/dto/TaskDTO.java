package com.habit.app.dto;

import com.habit.app.enums.TaskPriority;
import com.habit.app.enums.TaskStatus;
import com.habit.app.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
public class TaskDTO {

    private Long id;
    private String name;
    private TaskPriority priority;
    private TaskStatus status;
    private boolean isRepeating;
    private int repeatInterval;
    private LocalTime repeatTime;
    private LocalDateTime createTime;
    private LocalDateTime dueDate;
    private User user;
}
