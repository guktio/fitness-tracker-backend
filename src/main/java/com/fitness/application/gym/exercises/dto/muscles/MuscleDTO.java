package com.fitness.application.gym.exercises.dto.muscles;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MuscleDTO {
    private String code;
    private String name;
}
