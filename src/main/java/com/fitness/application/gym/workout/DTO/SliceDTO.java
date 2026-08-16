package com.fitness.application.gym.workout.DTO;

import java.util.List;

public record SliceDTO<D>(List<D> items, boolean hasNext, int pageNumber) {
    
}
