package com.fitness.application.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public UserNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
