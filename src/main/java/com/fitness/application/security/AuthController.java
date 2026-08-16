package com.fitness.application.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.application.users.UserMapper;
import com.fitness.application.users.UserService;
import com.fitness.application.users.DTO.UserRequestDTO;
import com.fitness.application.users.DTO.UserResponseDTO;
import com.fitness.application.users.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final UserMapper userMapper;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequestDTO dto) {
        log.info("POST /api/auth/login - Attempting login for user: {}", dto.getEmail());
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequestDTO dto) {
        log.info("POST /api/auth/register - Attempting registration for user: {}", dto.getEmail());
        return ResponseEntity.ok(authService.register(dto));
    }

    @GetMapping("/whoami")
    public ResponseEntity<UserResponseDTO> whoami(@CurrentUser User user) {
        log.info("GET /api/auth/whoami - Fetching current user info");
        return ResponseEntity.ok(userMapper.toResponseDto(userService.getUserOrThrowNotFound(user)));
    }

    @GetMapping("/isAlive")
    public ResponseEntity<String> isAlive() {
        log.info("GET /api/auth/isAlive - Checking if server is alive");
        return ResponseEntity.ok("Server is alive");
    }
}
