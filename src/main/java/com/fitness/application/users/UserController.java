package com.fitness.application.users;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

import com.fitness.application.users.DTO.UserRequestDTO;
import com.fitness.application.users.DTO.UserResponseDTO;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/{uuid}")
    public ResponseEntity<UserResponseDTO> findByUuid(@PathVariable UUID uuid){
        logger.info("GET /users/{}", uuid.toString());
        return ResponseEntity.ok().body(userService.getByUuid(uuid));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{uuid}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID uuid, @RequestBody UserRequestDTO user){
        logger.info("PUT /users/{}", uuid.toString());
        return ResponseEntity.ok().body(userService.update(user, uuid));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO user){
        logger.info("POST /users {}", user.toString());
        return ResponseEntity.ok().body(userService.create(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<HttpStatus> delete(@PathVariable UUID uuid){
        logger.info("DELETE /users/{}", uuid.toString());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<Page<UserResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("GET /users?page={}&size={}", page, size);
        return ResponseEntity.ok().body(userService.getAll(page, size));
    }

    @GetMapping("/check/username")
    public ResponseEntity<Boolean> checkUsername(String username){
        logger.info("GET /check/username for {}", username);
        return ResponseEntity.ok().body(userService.checkUsername(username));
    }

    @GetMapping("/check/email")
    public ResponseEntity<Boolean> checkEmail(String email){
        logger.info("GET /check/email for {}", email);
        return ResponseEntity.ok().body(userService.checkEmail(email));
    }
}
