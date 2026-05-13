package com.app.quantitymeasurement;

import com.app.quantitymeasurement.model.auth.LoginRequest;
import com.app.quantitymeasurement.model.auth.RegisterRequest;
import com.app.quantitymeasurement.service.auth.IAuthService;
import com.app.quantitymeasurement.model.auth.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired IAuthService authService;

    @Test
    void register_shouldReturnJwtToken() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setName("Test User");
        req.setEmail("testuser_" + System.currentTimeMillis() + "@example.com");
        req.setPassword("password123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.email").value(req.getEmail()));
    }

    @Test
    void login_withValidCredentials_shouldReturnJwt() throws Exception {
        // Register first
        RegisterRequest reg = new RegisterRequest();
        reg.setName("Login Tester");
        reg.setEmail("logintest_" + System.currentTimeMillis() + "@example.com");
        reg.setPassword("securePass");
        authService.register(reg);

        // Login
        LoginRequest login = new LoginRequest();
        login.setEmail(reg.getEmail());
        login.setPassword("securePass");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.email").value(reg.getEmail()));
    }

    @Test
    void login_withWrongPassword_shouldReturn403() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setName("Bad Login");
        reg.setEmail("bad_" + System.currentTimeMillis() + "@example.com");
        reg.setPassword("correct");
        authService.register(reg);

        LoginRequest login = new LoginRequest();
        login.setEmail(reg.getEmail());
        login.setPassword("wrong");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void register_duplicateEmail_shouldReturn4xx() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setName("Dup User");
        req.setEmail("dup@example.com");
        req.setPassword("pass123");
        authService.register(req);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is4xxClientError());
    }
}
