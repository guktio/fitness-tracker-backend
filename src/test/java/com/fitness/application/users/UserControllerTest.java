package com.fitness.application.users;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.application.exceptions.GlobalExceptionHandler;
import com.fitness.application.security.CurrentUser;
import com.fitness.application.users.dto.UserRequestDTO;
import com.fitness.application.users.dto.UserResponseDTO;
import com.fitness.application.users.entity.User;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = User.builder()
                .uuid(userId)
                .email("test@example.com")
                .username("testuser")
                .build();

        HandlerMethodArgumentResolver currentUserResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(CurrentUser.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return testUser;
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(currentUserResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void findByUuid_shouldReturnUser() throws Exception {
        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .uuid(userId)
                .email("test@example.com")
                .username("testuser")
                .build();

        when(userService.getByUuid(userId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).getByUuid(userId);
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .username("updatedUser")
                .email("updated@example.com")
                .build();

        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .uuid(userId)
                .username("updatedUser")
                .email("updated@example.com")
                .build();

        when(userService.update(eq(testUser), eq(userId), any(UserRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));

        verify(userService).update(eq(testUser), eq(userId), any(UserRequestDTO.class));
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isOk());

        verify(userService).delete(testUser, userId);
    }

    @Test
    void checkUsername_shouldReturnBoolean() throws Exception {
        when(userService.checkUsername("unique_name")).thenReturn(true);

        mockMvc.perform(get("/api/users/check/username?username=unique_name"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService).checkUsername("unique_name");
    }

    @Test
    void checkEmail_shouldReturnBoolean() throws Exception {
        when(userService.checkEmail("unique@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/users/check/email?email=unique@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService).checkEmail("unique@example.com");
    }
}

