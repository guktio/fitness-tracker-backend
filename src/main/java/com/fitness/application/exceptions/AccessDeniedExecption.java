package com.fitness.application.exceptions;

public class AccessDeniedExecption extends RuntimeException{
    public AccessDeniedExecption(String errorMessage) {
        super(errorMessage);
    }

    public AccessDeniedExecption(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}