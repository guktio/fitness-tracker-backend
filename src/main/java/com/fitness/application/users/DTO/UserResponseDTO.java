package com.fitness.application.users.DTO;

import java.util.Set;
import java.util.UUID;

import com.fitness.application.users.entity.Roles;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDTO {
    private UUID uuid;
    private String username;
    private String email;
    private Set<Roles> roles;
}
