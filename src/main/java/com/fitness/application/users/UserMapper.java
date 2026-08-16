package com.fitness.application.users;

import org.springframework.stereotype.Component;

import com.fitness.application.users.DTO.UserRequestDTO;
import com.fitness.application.users.DTO.UserResponseDTO;
import com.fitness.application.users.entity.User;

@Component
public class UserMapper {
    
    public UserResponseDTO toResponseDto(User entity){
        UserResponseDTO dto = UserResponseDTO.builder()
                                .uuid(entity.getUuid())
                                .username(entity.getUsername())
                                .email(entity.getEmail())
                                .roles(entity.getRoles())
                                .build();
        return dto;
    }

    public User toEntity(UserRequestDTO dto){
        User entity = User.builder()
                                .username(dto.getUsername())
                                .email(dto.getEmail())
                                .password(dto.getPassword())
                                .build();
        return entity;
    }
}

