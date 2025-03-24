package com.habit.app.controller;

import com.habit.app.dto.UserDTO;
import com.habit.app.enums.RoleName;
import com.habit.app.model.User;
import com.habit.app.service.UserRegisterRestriction;
import com.habit.app.service.UserRoleService;
import com.habit.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashSet;
import java.util.Set;

@RestController
public class RegisterController {

    UserService userService;
    AuthenticationManager authenticationManager;
    UserRoleService userRoleService;
    PasswordEncoder passwordEncoder;


    @Autowired
    public RegisterController(UserService userService, AuthenticationManager authenticationManager, UserRoleService userRoleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ModelAndView userRegister(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam String repeatPassword) {
        ModelAndView modelAndView = new ModelAndView("register");
        boolean hasError = false;

        if (userService.checkIfUserExistsByUsername(username)) {
            modelAndView.addObject("usernameError", "Username already taken");
            hasError = true;
        } else if (!UserRegisterRestriction.isValidUsername(username)) {
            modelAndView.addObject("usernameError", "Username needs to meet requirements");
            hasError = true;
        }

        if (userService.checkIfUserExistsByEmail(email)) {
            modelAndView.addObject("emailError", "Email already taken");
            hasError = true;
        } else if (!UserRegisterRestriction.isValidEmail(email)) {
            modelAndView.addObject("emailError", "Email needs to meet requirements");
            hasError = true;
        }

        if (!UserRegisterRestriction.isValidPassword(password)) {
            modelAndView.addObject("passwordError", "Password needs to meet requirements");
            hasError = true;
        }
        if(!UserRegisterRestriction.isPasswordMatch(password, repeatPassword))
        {
            modelAndView.addObject("repeatPasswordError", "Passwords do not match!");
            hasError = true;
        }

        if (hasError) {
            return modelAndView;  // Return to the register page with error messages
        }

        UserDTO newUser = new UserDTO();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);

        // Persist the user to the database and retrieve the saved UserDTO with an ID
        UserDTO createdUser = userService.createUser(newUser);

        // Set up the role after ensuring the user is saved
        userService.setUpUserRole(createdUser);

        modelAndView.setViewName("redirect:/RegistrationSuccess");  // Redirect to a success page
        return modelAndView;

    }
    @GetMapping("/showRegister")
    public ModelAndView userRegisterPage()
    {
        ModelAndView modelAndView = new ModelAndView("register");

        return modelAndView;
    }
}
