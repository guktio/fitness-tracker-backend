package com.fitness.application.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.application.users.UserMapper;
import com.fitness.application.users.DTO.UserRequestDTO;
import com.fitness.application.users.DTO.UserResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final UserMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequestDTO dto) {
        logger.info("POST /api/auth/login - Attempting login for user: {}", dto.getEmail());
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequestDTO dto) {
        logger.info("POST /api/auth/register - Attempting registration for user: {}", dto.getEmail());
        return ResponseEntity.ok(authService.register(dto));
    }

    @GetMapping("/whoami")
    public ResponseEntity<UserResponseDTO> whoami(Authentication authentication) {
        logger.info("GET /api/auth/whoami - Fetching current user info");
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userMapper.toResponseDto(principal.getUser()));
    }

    @GetMapping("/isAlive")
    public ResponseEntity<String> isAlive() {
        logger.info("GET /api/auth/isAlive - Checking if server is alive");
        return ResponseEntity.ok("Server is alive");
    }
}
