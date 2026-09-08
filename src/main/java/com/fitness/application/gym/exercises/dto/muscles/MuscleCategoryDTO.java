package com.fitness.application.gym.exercises.dto.muscles;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MuscleCategoryDTO {
    private String code;
    private String name;
    private Set<MuscleDTO> muscles;
}
