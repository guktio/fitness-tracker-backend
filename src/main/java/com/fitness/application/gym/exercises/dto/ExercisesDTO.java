package com.fitness.application.gym.exercises.dto;

import java.util.Set;
import java.util.UUID;

import lombok.Builder;

public final class ExercisesDTO {

    private ExercisesDTO() {}

    @Builder
    public record Exercise(
            Long id,
            String name,
            String description,
            ExerciseMuscle muscleGroup,
            String username,
            UUID userUuid
    ) {}

    public record CreateExercise(
            String name,
            String description,
            ExerciseMuscle muscles
    ) {}

    @Builder
    public record ExerciseMuscle(
            Set<MuscleImpact> primaryMuscles,
            Set<MuscleImpact> secondaryMuscles,
            Set<MuscleImpact> stabilizingMuscles
    ) {}

    @Builder
    public record MuscleImpact(
            String code,
            Integer impactLevel
    ) {}

    public record Muscle(
            String code,
            String name
    ) {}

    public record MuscleCategory(
            String code,
            String name,
            Set<Muscle> muscles
    ) {}
}