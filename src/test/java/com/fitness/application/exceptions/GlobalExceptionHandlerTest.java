package com.fitness.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleEntityNotFoundException_shouldReturn404() {
        EntityNotFoundException ex = new EntityNotFoundException("User not found");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleEntityNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("User not found", response.getBody().get("message"));
    }

    @Test
    void handleAccessDeniedException_shouldReturn403() {
        AccessDeniedException ex = new AccessDeniedException("Forbidden action");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleAccessDeniedExecption(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Access denied", response.getBody().get("error"));
        assertEquals("Forbidden action", response.getBody().get("message"));
    }

    @Test
    void handleAuthenticationException_shouldReturn401() {
        AuthenticationException ex = new BadCredentialsException("Bad credentials");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleAuthenticationException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unauthorized", response.getBody().get("error"));
        assertEquals("Invalid email or password", response.getBody().get("message"));
    }

    @Test
    void handleEntityExistsException_shouldReturn400() {
        EntityExistsException ex = new EntityExistsException("Already exists");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleEntityExistsException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EntityExistsException", response.getBody().get("error"));
        assertEquals("Entity already exists", response.getBody().get("message"));
    }

    @Test
    void handleMethodArgumentNotValidException_shouldReturn400WithFieldErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("user", "email", "Wrong email format");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleMethodArgumentNotValidException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Wrong email format", response.getBody().get("email"));
    }
}

