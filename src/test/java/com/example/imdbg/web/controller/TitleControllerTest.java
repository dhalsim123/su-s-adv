package com.example.imdbg.web.controller;

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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TitleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getById_LoggedIn() throws Exception {

        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(MockMvcRequestBuilders.get("/title/" + 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("titleViewDTO"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("principalWatchlistIds"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("currentDate"))
                .andExpect(MockMvcResultMatchers.view().name("titlePage"));
    }

    @Test
    void getById_Anonymous() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/title/" + 1L)
                        .with(SecurityMockMvcRequestPostProcessors.anonymous()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("titleViewDTO"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("currentDate"))
                .andExpect(MockMvcResultMatchers.view().name("titlePage"));
    }
}

//With Mock, not autowired


//@SpringBootTest
//@AutoConfigureMockMvc
//class TitleControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TitleService titleService;
//
//    @MockBean
//    private UserService userService;
//
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    void getById_LoggedIn() throws Exception {
//        TitleViewDTO testTitleViewDTO = new TitleViewDTO();
//        testTitleViewDTO.setId(1L);
//        testTitleViewDTO.setActors(new ArrayList<>());
//        testTitleViewDTO.setDirectors(new ArrayList<>());
//        testTitleViewDTO.setWriters(new ArrayList<>());
//
//        User testUser = new User("testUser", "", new ArrayList<>());
//        Principal principal = new UsernamePasswordAuthenticationToken(testUser, null);
//
//        when(titleService.getTitleViewDTOById(1L)).thenReturn(testTitleViewDTO);
//        when(userService.getPrincipalWatchlistIds(principal)).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/title/" + 1L)
//                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attributeExists("titleViewDTO"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("principalWatchlistIds"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("currentDate"))
//                .andExpect(MockMvcResultMatchers.view().name("titlePage"));
//    }
//}
//
