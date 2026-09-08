package com.fitness.application.base.dto;

import java.util.List;

public record SliceDTO<D>(List<D> items, boolean hasNext, int pageNumber) {
    
}
