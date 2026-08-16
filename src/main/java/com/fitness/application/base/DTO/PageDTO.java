package com.fitness.application.base.DTO;

import java.util.List;

public record PageDTO<D>(List<D> content, int page, int pageNum) {
    
}
