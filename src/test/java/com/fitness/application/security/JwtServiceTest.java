package com.fitness.application.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fitness.application.users.entity.Roles;
import com.fitness.application.users.entity.User;

class JwtServiceTest {

    private static final String SECRET_KEY = "test-super-secret-key-that-must-be-at-least-32-bytes-long!";
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET_KEY);
    }

    @Test
    void constructor_withNullOrBlankKey_shouldThrowException() {
        assertThrows(IllegalStateException.class, () -> new JwtService(null));
        assertThrows(IllegalStateException.class, () -> new JwtService("   "));
    }

    @Test
    void generateJwt_and_validateJwt_withValidUser_shouldSucceed() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .uuid(userId)
                .email("test@example.com")
                .roles(Set.of(Roles.USER))
                .build();

        String token = jwtService.generateJwt(user);

        assertNotNull(token);
        assertTrue(jwtService.validateJwt(token));
        assertEquals(userId.toString(), jwtService.extractUuid(token));
    }

    @Test
    void validateJwt_withInvalidOrTamperedToken_shouldReturnFalse() {
        assertFalse(jwtService.validateJwt("invalid.token.string"));

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .uuid(userId)
                .email("test@example.com")
                .roles(Set.of(Roles.USER))
                .build();

        String token = jwtService.generateJwt(user);
        String tamperedToken = token + "corrupted";

        assertFalse(jwtService.validateJwt(tamperedToken));
    }
}

