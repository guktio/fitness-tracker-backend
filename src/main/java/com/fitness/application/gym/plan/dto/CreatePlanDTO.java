package com.fitness.application.gym.plan.dto;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder
public class CreatePlanDTO {
    private String title;

    private String description;
}
