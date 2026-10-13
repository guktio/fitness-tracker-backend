package com.fitness.application.gym.plan;

import java.util.List;

import com.fitness.application.gym.exercises.dto.ExercisesDTO;

import lombok.Builder;

public final class PlansDTO {

    private PlansDTO() {}
    
    @Builder
    public record CreatePlan(String title,String description) {}

    @Builder
    public  record PlanExercise(
        Long id, 
        Integer orderNum, 
        ExercisesDTO.Exercise exercise,
        Plan plan
    ) {}

    @Builder 
    public record Plan(
        Long id,
        String title,
        String description,
        List<PlansDTO.PlanExercise> exercises
    ) {}
}