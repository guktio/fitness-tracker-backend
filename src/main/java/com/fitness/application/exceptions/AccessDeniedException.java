package com.fitness.application.exceptions;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String errorMessage) {
        super(errorMessage);
    }

    public AccessDeniedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}