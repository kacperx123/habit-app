package com.habit.app.service;

import com.habit.app.dto.UserRoleDTO;
import com.habit.app.enums.RoleName;
import com.habit.app.model.UserRole;
import com.habit.app.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRoleServiceTest {

    @Mock
    private UserRoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserRoleService userRoleService;

    private UserRole userRole;
    private UserRoleDTO userRoleDTO;

    @BeforeEach
    void setUp() {
        userRole = new UserRole();
        userRole.setId(1L);
        userRole.setRoleName(RoleName.ROLE_USER);

        userRoleDTO = new UserRoleDTO();
        userRoleDTO.setId(1L);
        userRoleDTO.setRoleName(RoleName.ROLE_USER);
    }

    @Test
    @DisplayName("Test - Get All Roles")
    void testGetAllRoles() {
        when(roleRepository.findAll()).thenReturn(List.of(userRole));
        when(modelMapper.map(any(UserRole.class), eq(UserRoleDTO.class))).thenReturn(userRoleDTO);

        List<UserRoleDTO> roles = userRoleService.getAllRoles();

        assertThat(roles).hasSize(1);
        assertThat(roles.get(0).getRoleName()).isEqualTo(RoleName.ROLE_USER);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test - Get Role By Id")
    void testGetRoleById() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(userRole));
        when(modelMapper.map(any(UserRole.class), eq(UserRoleDTO.class))).thenReturn(userRoleDTO);

        UserRoleDTO foundRole = userRoleService.getRoleById(1L);

        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getRoleName()).isEqualTo(RoleName.ROLE_USER);
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test - Get Role By Id - Role Not Found")
    void testGetRoleById_RoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userRoleService.getRoleById(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Role not found");
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(roles = "ROLE_ADMIN")
    @DisplayName("Test - Create Role as Admin")
    void testCreateRole_AsAdmin() {
        when(modelMapper.map(any(UserRoleDTO.class), eq(UserRole.class))).thenReturn(userRole);
        when(roleRepository.save(any(UserRole.class))).thenReturn(userRole);
        when(modelMapper.map(any(UserRole.class), eq(UserRoleDTO.class))).thenReturn(userRoleDTO);

        UserRoleDTO createdRole = userRoleService.createRole(userRoleDTO);

        assertThat(createdRole).isNotNull();
        assertThat(createdRole.getRoleName()).isEqualTo(RoleName.ROLE_USER);
        verify(roleRepository, times(1)).save(any(UserRole.class));
    }


    @Test
    @WithMockUser(roles = "ROLE_ADMIN")
    @DisplayName("Test - Update Role as Admin")
    void testUpdateRole_AsAdmin() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(userRole));
        when(roleRepository.save(any(UserRole.class))).thenReturn(userRole);
        when(modelMapper.map(any(UserRole.class), eq(UserRoleDTO.class))).thenReturn(userRoleDTO);

        userRoleDTO.setRoleName(RoleName.ROLE_ADMIN);
        UserRoleDTO updatedRole = userRoleService.updateRole(1L, userRoleDTO);

        assertThat(updatedRole).isNotNull();
        assertThat(updatedRole.getRoleName()).isEqualTo(RoleName.ROLE_ADMIN);
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(any(UserRole.class));
    }


    @Test
    @WithMockUser(roles = "ROLE_ADMIN")
    @DisplayName("Test - Delete Role as Admin")
    void testDeleteRole_AsAdmin() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(userRole));

        userRoleService.deleteRole(1L);

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).delete(userRole);
    }


}
