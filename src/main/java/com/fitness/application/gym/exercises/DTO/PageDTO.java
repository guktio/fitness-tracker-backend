package com.fitness.application.gym.exercises.DTO;

import java.util.List;

public record PageDTO<D>(List<D> content, int page, int pageNum) {
    
}
