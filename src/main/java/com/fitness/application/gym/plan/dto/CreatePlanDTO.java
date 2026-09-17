package com.fitness.application.gym.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class CreatePlanDTO {
    private String title;

    private String description;
}
