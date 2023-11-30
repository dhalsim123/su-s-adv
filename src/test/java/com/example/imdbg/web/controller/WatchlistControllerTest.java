package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.TitleEntity;
import com.example.imdbg.model.entity.users.UserEntity;
import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.users.UserService;
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

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WatchlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private TitleService titleService;
    @Test
    void getUserWatchlist() throws Exception {

        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(MockMvcRequestBuilders.get("/watchlist")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("watchlist"))
                .andExpect(MockMvcResultMatchers.view().name("userWatchlist"));

    }

    @Test
    void postAdd() throws Exception {

        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(MockMvcRequestBuilders.post("/watchlist/add/" + 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void postAdd_AlreadyInWatchlist_400() throws Exception {


        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        UserEntity userByUsername = userService.findUserByUsername("admin123");
        TitleEntity titleById = titleService.findTitleById(1L);

        userByUsername.setWatchlist(List.of(titleById));
        userService.saveUser(userByUsername);

        mockMvc.perform(MockMvcRequestBuilders.post("/watchlist/add/" + 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void postRemove() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        UserEntity userByUsername = userService.findUserByUsername("admin123");
        TitleEntity titleById = titleService.findTitleById(1L);

        userByUsername.setWatchlist(List.of(titleById));
        userService.saveUser(userByUsername);

        mockMvc.perform(MockMvcRequestBuilders.post("/watchlist/remove/" + 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void postRemove_NotInWatchlist_400() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(MockMvcRequestBuilders.post("/watchlist/remove/" + 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}