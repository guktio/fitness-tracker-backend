package com.fitness.application.gym.set;

import org.springframework.stereotype.Service;

@Service 
public class SetMapper {

    public SetDTO toSetDTO(WorkoutSet s) {
        return SetDTO.builder()
                .id(s.getId())
                .setNumber(s.getSetNumber())
                .weight(s.getWeight())
                .reps(s.getReps())
                .rpe(s.getRpe())
                .build();
    }
    
}
