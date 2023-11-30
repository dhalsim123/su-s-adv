package com.example.imdbg.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/person/" + 1L))
                .andExpect(MockMvcResultMatchers.model().attributeExists("personPageViewDTO"))
                .andExpect(MockMvcResultMatchers.view().name("personPage"));
    }
}