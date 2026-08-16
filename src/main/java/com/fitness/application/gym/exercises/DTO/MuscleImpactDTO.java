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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MuscleImpactDTO {
    private String muscle;
    private String name;
    private Integer impactLevel;
}
