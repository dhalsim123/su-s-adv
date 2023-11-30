package com.example.imdbg.web.controller.admin;

import com.example.imdbg.model.entity.users.UserEntity;
import com.example.imdbg.model.entity.users.dto.binding.UserRegisterDTO;
import com.example.imdbg.service.admin.AdminService;
import com.example.imdbg.service.users.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Test
    void getAdminPanel() throws Exception {

        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/panel")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.view().name("adminPanel"));
    }

    @Test
    void getAllUsers() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/all")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allUserSettingsDTO"))
                .andExpect(MockMvcResultMatchers.view().name("adminPanel-allUsers"));
    }

    @Test
    void getUserById() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/" + 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("adminPanel-user-by-id"));
    }

    @Test
    void removeAdminRole() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        Principal principal = new UsernamePasswordAuthenticationToken(testUser, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        UserRegisterDTO testAdmin = new UserRegisterDTO();
        testAdmin.setPassword("testAdminRole1");
        testAdmin.setConfirmPassword("testAdminRole1");
        testAdmin.setUsername("testAdminRole1");
        testAdmin.setEmail("testAdminRole1@testAdmin.com");

        userService.registerUser(testAdmin);

        UserEntity testAdmin1 = userService.findUserByUsername("testAdminRole1");

        adminService.addAdminRole(testAdmin1.getId(), principal);

        testAdmin1 = userService.findUserByUsername("testAdminRole1");
        Assertions.assertTrue(testAdmin1.getRoles().stream().anyMatch(roleEntity -> roleEntity.getRole().name().equals("ADMIN")));


        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/" + testAdmin1.getId() + "/remove-admin-role")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/users/" + testAdmin1.getId()));

        testAdmin1 = userService.findUserByUsername("testAdminRole1");
        Assertions.assertFalse(testAdmin1.getRoles().stream().anyMatch(roleEntity -> roleEntity.getRole().name().equals("ADMIN")));

    }

    @Test
    void addAdminRole() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        UserRegisterDTO testAdmin = new UserRegisterDTO();
        testAdmin.setPassword("testAdminRole123");
        testAdmin.setConfirmPassword("testAdminRole123");
        testAdmin.setUsername("testAdminRole123");
        testAdmin.setEmail("testAdminRole123@testAdmin.com");

        userService.registerUser(testAdmin);

        UserEntity testAdmin1 = userService.findUserByUsername("testAdminRole123");

        Assertions.assertFalse(testAdmin1.getRoles().stream().anyMatch(roleEntity -> roleEntity.getRole().name().equals("ADMIN")));


        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/" + testAdmin1.getId() + "/add-admin-role")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/users/" + testAdmin1.getId()));

        testAdmin1 = userService.findUserByUsername("testAdminRole123");
        Assertions.assertTrue(testAdmin1.getRoles().stream().anyMatch(roleEntity -> roleEntity.getRole().name().equals("ADMIN")));
    }
}