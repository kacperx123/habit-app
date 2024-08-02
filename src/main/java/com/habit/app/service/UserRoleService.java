package com.habit.app.service;
import com.habit.app.dto.UserRoleDTO;
import com.habit.app.model.UserRole;
import com.habit.app.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    private final UserRoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserRoleService(UserRoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<UserRoleDTO> getAllRoles() {
        List<UserRole> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> modelMapper.map(role, UserRoleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserRoleDTO getRoleById(Long id) {
        UserRole role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return modelMapper.map(role, UserRoleDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserRoleDTO createRole(UserRoleDTO roleDTO) {
        UserRole role = modelMapper.map(roleDTO, UserRole.class);
        UserRole savedRole = roleRepository.save(role);
        return modelMapper.map(savedRole, UserRoleDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserRoleDTO updateRole(Long id, UserRoleDTO roleDTO) {
        UserRole existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        existingRole.setRoleName(roleDTO.getRoleName());
        UserRole updatedRole = roleRepository.save(existingRole);
        return modelMapper.map(updatedRole, UserRoleDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteRole(Long id) {
        UserRole role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(role);
    }
}

