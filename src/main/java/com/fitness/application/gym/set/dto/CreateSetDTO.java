package com.fitness.application.gym.set.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class CreateSetDTO {
    private Integer setNumber;
    private Double weight;
    private Integer reps;
    private Double rpe;
}
