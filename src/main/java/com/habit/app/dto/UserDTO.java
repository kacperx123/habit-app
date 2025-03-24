package com.habit.app.dto;


import com.habit.app.model.Task;
import com.habit.app.model.UserRole;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Set<UserRole> roles;
    private List<Task> tasks;
}
