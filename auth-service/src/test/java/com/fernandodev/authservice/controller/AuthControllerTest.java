package com.fernandodev.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernandodev.authservice.dto.AuthResponseDto;
import com.fernandodev.authservice.dto.UserLoginRequestDto;
import com.fernandodev.authservice.dto.UserRegistrationRequestDto;
import com.fernandodev.authservice.repository.UserRepository;
import com.fernandodev.authservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import com.fernandodev.authservice.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    private static final String API_BASE_URL = "/api/v1/auth";
    private static final String REGISTER_URL = API_BASE_URL + "/register";
    private static final String LOGIN_URL = API_BASE_URL + "/login";
    private static final String TOKEN_JSON_PATH = "$.token";
    private static final String TOKEN_VALUE = "token";

    @TestConfiguration
    static class AuthControllerTestConfiguration {
        @Bean
        @Primary
        public AuthService authService(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder,
                                       JwtTokenProvider jwtTokenProvider,
                                       AuthenticationManager authenticationManager) {
            return Mockito.spy(new AuthService(userRepository, passwordEncoder, jwtTokenProvider, authenticationManager));
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    private AuthResponseDto authResponseDto;

    @BeforeEach
    void setUp() {
        authResponseDto = new AuthResponseDto(TOKEN_VALUE);
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto("testuser", "test@test.com", "password");
        doReturn(authResponseDto).when(authService).registerUser(any(UserRegistrationRequestDto.class));

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(TOKEN_JSON_PATH).value(TOKEN_VALUE));
    }

    @Test
    public void shouldLoginUser() throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto("test@test.com", "password");
        doReturn(authResponseDto).when(authService).login(any(UserLoginRequestDto.class));

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TOKEN_JSON_PATH).value(TOKEN_VALUE));
    }
}
