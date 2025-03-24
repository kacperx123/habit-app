package com.habit.app.controller;

import com.habit.app.service.UserRoleService;
import com.habit.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRoleService userRoleService;

    @Autowired
    public LoginController(UserService userService, AuthenticationManager authenticationManager, UserRoleService userRoleService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRoleService = userRoleService;
    }

    @GetMapping("/showLogin")
    public ModelAndView userLoginPage(@RequestParam(value = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("login");
        if (error != null) {
            modelAndView.addObject("loginError", "Invalid email or password");  
        }
        return modelAndView;
    }

}
