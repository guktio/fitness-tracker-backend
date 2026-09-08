package com.fitness.application.gym.plan.DTO;

import com.fitness.application.gym.exercises.DTO.ExerciseDTO;
import com.fitness.application.gym.plan.entity.WorkoutPlan;

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
