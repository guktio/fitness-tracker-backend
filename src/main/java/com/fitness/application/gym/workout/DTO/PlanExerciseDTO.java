package com.fitness.application.gym.workout.DTO;

import com.fitness.application.gym.exercises.DTO.ExerciseDTO;
import com.fitness.application.gym.workout.entity.WorkoutPlan;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class PlanExerciseDTO {
    private Long id;

    private Integer orderNum;

    private ExerciseDTO exercise;

    private WorkoutPlan workoutPlan;
}
