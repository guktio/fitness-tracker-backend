package com.fitness.application.gym.set;

import java.io.Serializable;

import lombok.Builder;

public final class SetsDTO {
    
    private SetsDTO() {}

    @Builder 
    public record Create (
        Integer setNumber,
        Double weight,
        Integer reps,
        Double rpe
    ) {}

    @Builder
    public record Set (
        Long id,
        Integer setNumber,
        Double weight,
        Integer reps,
        Double rpe
    ) implements Serializable {}
}
