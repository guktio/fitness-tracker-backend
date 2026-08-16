package com.fitness.application.exceptions;

import lombok.Data;

@Data
public class ExceptionDTO {

    String name;

    String message;

    String path;

    Long timestamp;

    
}
