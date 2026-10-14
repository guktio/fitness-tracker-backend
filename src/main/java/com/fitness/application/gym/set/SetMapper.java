package com.fitness.application.gym.set;

import org.springframework.stereotype.Service;

@Service 
public class SetMapper {

    public SetsDTO.Set toSetDTO(WorkoutSet s) {
        return SetsDTO.Set.builder()
                .id(s.getId())
                .setNumber(s.getSetNumber())
                .weight(s.getWeight())
                .reps(s.getReps())
                .rpe(s.getRpe())
                .build();
    }
}
