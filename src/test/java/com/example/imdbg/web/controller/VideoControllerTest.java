package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.VideoEntity;
import com.example.imdbg.repository.movies.VideoRepository;
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
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void getById() throws Exception {

        VideoEntity videoEntity = videoRepository.findById(1L).orElseThrow();

        mockMvc.perform(MockMvcRequestBuilders.get("/video/" + videoEntity.getVideoImdbId()))
                .andExpect(MockMvcResultMatchers.model().attributeExists("titleVideoViewDTO"))
                .andExpect(MockMvcResultMatchers.view().name("videoPage"));
    }
}