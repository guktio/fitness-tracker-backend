package com.fitness.application.gym.exercises.DTO;

import java.util.Set;

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
public class ExerciseMuscleDTO {
    private Set<MuscleImpactDTO> primaryMuscles;
    private Set<MuscleImpactDTO> secondaryMuscles;
    private Set<MuscleImpactDTO> stabilizingMuscles;

}
