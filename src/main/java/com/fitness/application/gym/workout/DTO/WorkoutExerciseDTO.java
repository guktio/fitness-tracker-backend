package com.fitness.application.gym.workout.DTO;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WorkoutExerciseDTO {
    private Long id;

    private Integer orderNum;
    private String exerciseName;

    private List<WorkoutSetDTO> set;
        
}
