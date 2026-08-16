package com.fitness.application.gym.workout.DTO;

import com.fitness.application.gym.workout.entity.Workout.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleWorkoutDTO {
    private Long id;
    
    private Status status;
    
    private String createdAt;
}
