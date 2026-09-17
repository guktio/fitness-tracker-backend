package com.fitness.application.gym.set.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SetDTO {
    private Long id;
    private Integer setNumber;
    private Double weight;
    private Integer reps;
    private Double rpe;
}
