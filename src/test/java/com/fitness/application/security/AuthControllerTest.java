package com.fitness.application.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.application.exceptions.GlobalExceptionHandler;
import com.fitness.application.users.UserMapper;
import com.fitness.application.users.UserService;
import com.fitness.application.users.dto.UserRequestDTO;
import com.fitness.application.users.dto.UserResponseDTO;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void isAlive_shouldReturnOkAndServerIsAlive() throws Exception {
        mockMvc.perform(get("/api/auth/isAlive"))
                .andExpect(status().isOk())
                .andExpect(content().string("Server is alive"));
    }

    @Test
    void login_withValidPayload_shouldReturnOkAndAuthResponse() throws Exception {
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        UserResponseDTO userDTO = UserResponseDTO.builder()
                .uuid(UUID.randomUUID())
                .email("test@example.com")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token("test.jwt.token")
                .user(userDTO)
                .build();

        when(authService.login(any(UserRequestDTO.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test.jwt.token"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));

        verify(authService).login(any(UserRequestDTO.class));
    }

    @Test
    void register_withValidPayload_shouldReturnOkAndAuthResponse() throws Exception {
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .email("new@example.com")
                .username("newuser")
                .password("password123")
                .build();

        UserResponseDTO userDTO = UserResponseDTO.builder()
                .uuid(UUID.randomUUID())
                .email("new@example.com")
                .username("newuser")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token("new.jwt.token")
                .user(userDTO)
                .build();

        when(authService.register(any(UserRequestDTO.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new.jwt.token"))
                .andExpect(jsonPath("$.user.email").value("new@example.com"));

        verify(authService).register(any(UserRequestDTO.class));
    }
}

