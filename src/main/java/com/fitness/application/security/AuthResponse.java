package com.fitness.application.security;

import com.fitness.application.users.DTO.UserResponseDTO;

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