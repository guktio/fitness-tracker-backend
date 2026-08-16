package com.fitness.application.gym.workout.DTO;

import java.util.List;

import com.fitness.application.gym.workout.entity.Workout;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WorkoutInfo {

    private Workout workout;
    
    private List<WorkoutExerciseDTO> exerciseDTO;

    private String createdAt;
}
