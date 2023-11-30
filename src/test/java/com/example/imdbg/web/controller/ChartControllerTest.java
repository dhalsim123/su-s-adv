package com.example.imdbg.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ChartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void get100MostPopular() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/chart/100MostPopular"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("titles"))
                .andExpect(MockMvcResultMatchers.view().name("100MostPopular"));
    }

    @Test
    void getTop250() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/chart/top250"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("titles"))
                .andExpect(MockMvcResultMatchers.view().name("top250"));
    }
}