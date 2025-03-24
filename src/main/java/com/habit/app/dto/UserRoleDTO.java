package com.habit.app.dto;

import com.habit.app.enums.RoleName;
import com.habit.app.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
public class UserRoleDTO {

    private Long id;
    private RoleName roleName;
    private User user;

}
