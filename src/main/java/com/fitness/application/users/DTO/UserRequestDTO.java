package com.fitness.application.users.DTO;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class UserRequestDTO {
    @Email(message = "Wrong email format")
    private String email;
    private String username;
    private String password;
}
