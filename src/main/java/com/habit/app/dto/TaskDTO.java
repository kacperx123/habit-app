package com.habit.app.dto;

import com.habit.app.enums.TaskPriority;
import com.habit.app.model.User;
import lombok.Data;


@Data
public class TaskDTO {

    private Long id;
    private String name;
    private TaskPriority priority;
    private User user;
}
