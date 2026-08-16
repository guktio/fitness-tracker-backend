package com.fitness.application.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException( 
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Map<String, String>> handleEntityExistsException( 
        EntityExistsException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        errors.put("error", ex.getClass().getSimpleName());
        errors.put("message", "Entity already exists");
        // ex.getBindingResult().getFieldErrors().forEach(error -> {
        //     errors.put(error.getField(), error.getDefaultMessage());
        // });
        
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(
        AuthenticationException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        errors.put("error", "Unauthorized");
        errors.put("message", "Invalid email or password");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(
        EntityNotFoundException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        errors.put("error", "Not Found");
        errors.put("message", ex.getMessage());
        logger.info("Handled EntityNotFoundException");
        logger.debug(ex.toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }
}
