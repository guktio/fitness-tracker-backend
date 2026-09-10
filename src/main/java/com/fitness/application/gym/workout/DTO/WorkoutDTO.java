package com.fitness.application.gym.workout.DTO;

import com.fitness.application.gym.workout.entity.Workout.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDTO {
    private Long id;
    
    private Status status;
    
    private String createdAt;
}
