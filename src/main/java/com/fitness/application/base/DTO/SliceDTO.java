package com.fitness.application.base.DTO;

import java.util.List;

public record SliceDTO<D>(List<D> items, boolean hasNext, int pageNumber) {
    
}
