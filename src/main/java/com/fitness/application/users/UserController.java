package com.fitness.application.users;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.application.base.DTO.PageDTO;
import com.fitness.application.users.DTO.UserRequestDTO;
import com.fitness.application.users.DTO.UserResponseDTO;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    
    private final UserService userService;

    @GetMapping("/{uuid}")
    public ResponseEntity<UserResponseDTO> findByUuid(@PathVariable UUID uuid){
        log.info("GET /users/{}", uuid.toString());
        return ResponseEntity.ok().body(userService.getByUuid(uuid));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{uuid}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID uuid, @RequestBody UserRequestDTO user){
        log.info("PUT /users/{}", uuid.toString());
        return ResponseEntity.ok().body(userService.update(user, uuid));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO user){
        log.info("POST /users {}", user.toString());
        return ResponseEntity.ok().body(userService.create(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<HttpStatus> delete(@PathVariable UUID uuid){
        log.info("DELETE /users/{}", uuid.toString());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<PageDTO<UserResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /users?page={}&size={}", page, size);
        return ResponseEntity.ok().body(userService.getAll(page, size));
    }

    @GetMapping("/check/username")
    public ResponseEntity<Boolean> checkUsername(String username){
        log.info("GET /check/username for {}", username);
        return ResponseEntity.ok().body(userService.checkUsername(username));
    }

    @GetMapping("/check/email")
    public ResponseEntity<Boolean> checkEmail(String email){
        log.info("GET /check/email for {}", email);
        return ResponseEntity.ok().body(userService.checkEmail(email));
    }
}
