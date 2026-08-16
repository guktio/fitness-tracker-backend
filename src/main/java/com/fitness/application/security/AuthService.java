package com.fitness.application.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;

import com.fitness.application.users.UserMapper;
import com.fitness.application.users.UserService;
import com.fitness.application.users.DTO.UserRequestDTO;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final JwtService jwtService;

    private final UserService userService;

    private final UserMapper userMapper;
    
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;


    public AuthResponse register(UserRequestDTO dto) {
        if (userService.existsByEmail(dto.getEmail())) {
            throw new EntityExistsException("User exists");
        }

        String rawPassword = dto.getPassword();

        UserRequestDTO encodedDto = dto.toBuilder()
                .password(passwordEncoder.encode(rawPassword))
                .build();
        userService.create(encodedDto);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), rawPassword);
                
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
        User user = principal.getUser();

        String token = jwtService.generateJwt(user);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(userMapper.toResponseDto(user));
        return response;
    }
    
    public AuthResponse login(UserRequestDTO dto) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
            
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
        User user = principal.getUser();

        String token = jwtService.generateJwt(user);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(userMapper.toResponseDto(user));
        return response;
    }
}
