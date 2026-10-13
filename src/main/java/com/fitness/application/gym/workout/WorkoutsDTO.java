package com.fitness.application.gym.workout;

import java.util.List;

import com.fitness.application.gym.set.SetsDTO;
import com.fitness.application.gym.workout.entity.Workout.Status;

import lombok.Builder;

public final class WorkoutsDTO {

    private WorkoutsDTO() {}

    @Builder 
    public record Workout(
        Long id,
        Status status,
        String createdAt
    ) {}
    
    @Builder 
    public record WorkoutExercise(
        Long id,
        Integer orderNum,
        String exerciseName,
        List<SetsDTO.Set> sets
    ) {}

    public record AddExercise(
        Integer orderNum 
    ){}

    @Builder 
    public record Info(
        Long id,
        Status status,
        List<WorkoutExercise> exercises,
        String createdAt
    ) {}
}
