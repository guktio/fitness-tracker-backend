package com.fitness.application.gym.set;

import org.springframework.stereotype.Service;

import com.fitness.application.gym.set.dto.SetDTO;

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
