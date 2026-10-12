package com.fitness.application.gym.exercises.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO implements Serializable{
    private Long id;
    private String name;
    private String description;
    private ExerciseMuscleDTO muscleGroup;
    private String username;
    private UUID userUuid;
}