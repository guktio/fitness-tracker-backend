package com.fitness.application.gym.plan.dto;

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
