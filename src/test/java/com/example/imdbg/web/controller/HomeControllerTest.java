package com.example.imdbg.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getHome_LoggedIn() throws Exception {

        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));


        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("topRatedCarouselViewDTOs"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("fourMostPopular"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("principalWatchlistIds"))
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    void getHome_Anonymous() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .with(SecurityMockMvcRequestPostProcessors.anonymous()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("topRatedCarouselViewDTOs"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("fourMostPopular"))
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }
}