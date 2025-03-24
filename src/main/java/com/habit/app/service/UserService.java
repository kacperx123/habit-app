package com.habit.app.service;

import com.habit.app.dto.UserDTO;
import com.habit.app.enums.RoleName;
import com.habit.app.model.User;
import com.habit.app.model.UserRole;
import com.habit.app.repository.UserRoleRepository;
import com.habit.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserRoleRepository roleRepository;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        System.out.println("Raw password before encoding: " + user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Encoded password stored in DB: " + user.getPassword());
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
    @Transactional
    public boolean checkIfUserExistsByUsername(String username) { // if exists returns true

        if (userRepository.existsByUsername(username))
            return true;
        else
            return false;
    }
    @Transactional
    public boolean checkIfUserExistsByEmail(String email) { // if exists returns true

        if (userRepository.existsByEmail(email))
            return true;
        else
            return false;
    }
    @Transactional
    public void setUpUserRole(UserDTO userDTO) {
        // Retrieve the User entity from the database using the ID from userDTO
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the UserRole with given userId and roleName already exists
        boolean roleExists = roleRepository.existsByUserIdAndRoleName(user.getId(), RoleName.ROLE_USER);

        // If the role doesn't exist, create and assign it to the user
        if (!roleExists) {
            UserRole userRole = new UserRole();
            userRole.setRoleName(RoleName.ROLE_USER);
            userRole.setUser(user);

            // Use addRole method to maintain bidirectional relationship
            user.addRole(userRole);
            // Save the role using the user repository to cascade the role save
            userRepository.save(user);  // Ensures both User and UserRole are saved
        }

}



    private String encodePassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        return encodedPassword;
    }
}


