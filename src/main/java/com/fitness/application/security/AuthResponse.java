package com.fitness.application.security;

import com.fitness.application.users.dto.UserResponseDTO;

import lombok.Builder;
import lombok.Data;

/**
 * AuthResponse
 */
@Data
@Builder
public class AuthResponse {

    UserResponseDTO user;

    String token;    
}