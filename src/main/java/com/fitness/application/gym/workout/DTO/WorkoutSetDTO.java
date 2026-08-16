package com.fitness.application.gym.workout.DTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WorkoutSetDTO {
    private Long id;
    private Integer setNumber;
    private Double weight;
    private Integer reps;
    private Double rpe;
}
