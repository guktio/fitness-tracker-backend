package com.fitness.application.gym.exercises.dto;

import java.util.Set;

import lombok.Builder;

public final class MusclesDTO {

    private MusclesDTO() {}

    @Builder 
    public record InnerMuscles(
        String code,
        String name,
        Set<Muscle> muscles
    ) {}

    @Builder 
    public record Muscle (
        String code
    ) {}
}
