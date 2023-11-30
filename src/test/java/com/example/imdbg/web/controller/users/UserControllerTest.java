package com.example.imdbg.web.controller.users;

import com.example.imdbg.model.entity.users.UserEntity;
import com.example.imdbg.service.users.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    @Test
    void getSettings() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/settings")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userSettingsDTO"))
                .andExpect(MockMvcResultMatchers.view().name("userSettings"));
    }

    @Test
    void getChangeUsername() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/settings/change-username")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.view().name("change-username"));
    }

    @Test
    @Transactional
    void patchChangeUsername() throws Exception {

        UserEntity userById = userService.findUserById(1L);

        Assertions.assertEquals("admin123", userById.getUsername());

        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(patch("/users/settings/change-username")
                        .param("username", "changingUsername123")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/settings"));

        userById = userService.findUserById(1L);

        Assertions.assertEquals("changingUsername123", userById.getUsername());

    }

    @Test
    void patchChangeUsername_Error() throws Exception {

        UserEntity userById = userService.findUserById(1L);

        Assertions.assertEquals("admin123", userById.getUsername());

        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(patch("/users/settings/change-username")
                        .param("username", "1!")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/settings/change-username"));

        userById = userService.findUserById(1L);

        Assertions.assertEquals("admin123", userById.getUsername());
        Assertions.assertNotEquals("1!", userById.getUsername());

    }
}