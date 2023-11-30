package com.example.imdbg.web.controller.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    void getRegister() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"));
    }

    @Test
    void postRegister() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "registerTest")
                        .param("email", "registerTest@email.com")
                        .param("password", "registerTest")
                        .param("confirmPassword", "registerTest")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void postRegister_Error() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "testFirstname!@!@")
                        .param("email", "test@email.com")
                        .param("password", "testPassword")
                        .param("confirmPassword", "testPassword123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }

}