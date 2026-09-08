package com.fitness.application.gym.workout.DTO;

import java.util.List;

import com.fitness.application.gym.set.SetDTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WorkoutExerciseDTO {
    private Long id;

    private Integer orderNum;
    private String exerciseName;

    private List<SetDTO> set;
        
}
