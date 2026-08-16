package com.fitness.application.security;

import com.fitness.application.users.DTO.UserResponseDTO;

import lombok.Data;

/**
 * AuthResponse
 */
@Data
public class AuthResponse {

    UserResponseDTO user;

    String token;    
}