package com.fitness.application.exceptions;

public class WorkoutStateException extends RuntimeException{
     public WorkoutStateException(String errorMessage) {
        super(errorMessage);
    }

    public WorkoutStateException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
