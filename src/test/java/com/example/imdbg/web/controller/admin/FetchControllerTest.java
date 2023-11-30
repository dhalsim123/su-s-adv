package com.example.imdbg.web.controller.admin;

import com.example.imdbg.service.movies.TitleService;
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
class FetchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TitleService titleService;


    @Test
    void getFetchLists() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/fetchIMDB/lists")
                .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("isThreadRunning"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("fetchStatus"))
                .andExpect(MockMvcResultMatchers.view().name("fetchIMDBLists"));
    }

    @Test
    void getFetchSingle() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/fetchIMDB/single")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("isThreadRunning"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("fetchStatus"))
                .andExpect(MockMvcResultMatchers.view().name("fetchIMDBSingle"));
    }

    @Test
    void getFetchPages() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/fetchIMDB/pages")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("isThreadRunning"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("fetchStatus"))
                .andExpect(MockMvcResultMatchers.view().name("fetchIMDBPages"));
    }

    @Test
    void getUpdateLists() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/fetchIMDB/updates")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("isThreadRunning"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("fetchStatus"))
                .andExpect(MockMvcResultMatchers.view().name("updateIMDBLists"));
    }

    @Test
    void getUpdateSingle() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/fetchIMDB/updates/single")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.model().attributeExists("isThreadRunning"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("fetchStatus"))
                .andExpect(MockMvcResultMatchers.view().name("updateIMDBSingle"));
    }

    @Test
    void getFetchStatus() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/fetchIMDB/status")
                        .header("Accept", "")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void postStopListsFetch() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/fetchIMDB/lists/stop")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/fetchIMDB/lists"));
    }

    @Test
    void postStopSingleFetch() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/fetchIMDB/single/stop")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/fetchIMDB/single"));
    }

    @Test
    void postStopPageFetch() throws Exception {
        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/fetchIMDB/pages/stop")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/fetchIMDB/pages"));
    }

    @Test
    void postStartUpdateSingleFetch() throws Exception {

        User testUser = new User("admin123", "admin123", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/fetchIMDB/start/updates/single/" + 1L)
                .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/fetchIMDB/updates/single"));
    }
}