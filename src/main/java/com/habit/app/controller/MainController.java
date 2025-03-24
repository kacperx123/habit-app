package com.habit.app.controller;

import com.habit.app.dto.TaskDTO;
import com.habit.app.enums.TaskPriority;
import com.habit.app.model.User;
import com.habit.app.security.CustomUserDetailsService;
import com.habit.app.service.TaskService;
import com.habit.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequestMapping("/main")
public class MainController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @GetMapping("/")
    public ModelAndView mainPage()
    {
        ModelAndView modelAndView = new ModelAndView("mainPage");
        return modelAndView;
    }

    @PostMapping("/tasks/create")
    public ResponseEntity<String> addTask(
            @RequestParam String name,
            @RequestParam String priority,
            Authentication authentication) {

        CustomUserDetailsService userDetails = (CustomUserDetailsService) authentication.getPrincipal();
        Long userId = userDetails.getUserIdFromPrincipal();

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName(name);
        taskDTO.setPriority(TaskPriority.valueOf(priority));


        taskService.createTask(userId, taskDTO);

        return ResponseEntity.ok("Task added successfully");
    }

}
