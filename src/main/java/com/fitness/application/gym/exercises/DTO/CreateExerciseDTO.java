package com.fitness.application.gym.exercises.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateExerciseDTO {
    private String name;
    private String description;
    private ExerciseMuscleDTO muscles;
    
}