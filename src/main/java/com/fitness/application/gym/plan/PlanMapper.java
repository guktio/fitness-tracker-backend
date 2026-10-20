package com.fitness.application.gym.plan;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fitness.application.gym.exercises.ExerciseMapper;
import com.fitness.application.gym.plan.entity.PlanExercise;
import com.fitness.application.gym.plan.entity.WorkoutPlan;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class PlanMapper {

    private final ExerciseMapper exerciseMapper;

    public PlansDTO.Plan toPlanDTO(WorkoutPlan workoutPlan) {
        return PlansDTO.Plan.builder()
                .id(workoutPlan.getId())
                .title(workoutPlan.getTitle())
                .description(workoutPlan.getDescription())
                .exercises(toPlanExerciseDTO(workoutPlan.getExercises()))
                .build();
    }

    public List<PlansDTO.PlanExercise> toPlanExerciseDTO(List<PlanExercise> exercises) {
        if (exercises == null) {
            return List.of();
        }
        return exercises.stream()
                .map(this::toPlanExerciseDTO)
                .collect(Collectors.toList());
    }

    public PlansDTO.PlanExercise toPlanExerciseDTO(PlanExercise pe) {
        return PlansDTO.PlanExercise.builder()
                .id(pe.getId())
                .orderNum(pe.getOrderNum())
                .exercise(exerciseMapper.toDTO(pe.getExercise()))
                .build();
    }

    public WorkoutPlan toEntity(PlansDTO.CreatePlan dto) {
        return WorkoutPlan.builder()
                        .title(dto.title())
                        .description(dto.description())
                        .build();
    }
}
