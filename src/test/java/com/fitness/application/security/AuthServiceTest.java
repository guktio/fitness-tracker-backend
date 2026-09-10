package com.fitness.application.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fitness.application.users.UserMapper;
import com.fitness.application.users.UserService;
import com.fitness.application.users.dto.UserRequestDTO;
import com.fitness.application.users.dto.UserResponseDTO;
import com.fitness.application.users.entity.Roles;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityExistsException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private UserRequestDTO userRequestDTO;
    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        userRequestDTO = UserRequestDTO.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password123")
                .build();

        user = User.builder()
                .uuid(userId)
                .email("test@example.com")
                .username("testuser")
                .roles(Set.of(Roles.USER))
                .build();

        userResponseDTO = UserResponseDTO.builder()
                .uuid(userId)
                .email("test@example.com")
                .username("testuser")
                .roles(Set.of(Roles.USER))
                .build();
    }

    @Test
    void register_whenEmailAlreadyExists_shouldThrowEntityExistsException() {
        when(userService.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> authService.register(userRequestDTO));
    }

    @Test
    void register_whenEmailIsAvailable_shouldRegisterAndReturnToken() {
        when(userService.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateJwt(user)).thenReturn("sample.jwt.token");
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDTO);

        AuthResponse response = authService.register(userRequestDTO);

        assertNotNull(response);
        assertEquals("sample.jwt.token", response.getToken());
        assertEquals("test@example.com", response.getUser().getEmail());

        verify(userService).create(any(UserRequestDTO.class));
        verify(jwtService).generateJwt(user);
    }

    @Test
    void login_withValidCredentials_shouldAuthenticateAndReturnToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateJwt(user)).thenReturn("sample.jwt.token");
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDTO);

        AuthResponse response = authService.login(userRequestDTO);

        assertNotNull(response);
        assertEquals("sample.jwt.token", response.getToken());
        assertEquals("test@example.com", response.getUser().getEmail());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateJwt(user);
    }
}

