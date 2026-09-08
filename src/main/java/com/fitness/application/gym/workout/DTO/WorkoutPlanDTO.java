package com.fitness.application.gym.workout.DTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder
public class WorkoutPlanDTO {
    
    private Long id;

    private String title;

    private String description;

    private List<PlanExerciseDTO> exercises;
}
