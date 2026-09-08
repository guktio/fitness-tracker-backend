package com.fitness.application.base.dto;

import java.util.List;

public record PageDTO<D>(List<D> content, int page, int totalPages) {
    
}
