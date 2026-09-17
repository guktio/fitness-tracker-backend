package com.fitness.application.gym.set.dto;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class CreateSetDTO {
    private Integer setNumber;
    private Double weight;
    private Integer reps;
    private Double rpe;
}
