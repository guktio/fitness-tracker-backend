package com.fitness.application.gym.workout;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fitness.application.gym.workout.DTO.WorkoutDTO;
import com.fitness.application.gym.workout.DTO.WorkoutExerciseDTO;
import com.fitness.application.gym.workout.DTO.WorkoutInfo;
import com.fitness.application.gym.workout.DTO.WorkoutSetDTO;
import com.fitness.application.gym.workout.entity.Workout;
import com.fitness.application.gym.workout.entity.WorkoutExercise;
import com.fitness.application.gym.workout.entity.WorkoutSet;

@Component
public class WorkoutMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

    private String prettyTime(Instant time){
        return DATE_TIME_FORMATTER.format(time.atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    public WorkoutInfo toWorkoutInfo(Workout workout) {
        List<WorkoutExerciseDTO> exerciseDTOs = workout.getExercises().stream()
                .map(we -> toExerciseDTO(we))
                .collect(Collectors.toList());

        return WorkoutInfo.builder()
                .id(workout.getId())
                .status(workout.getStatus())
                .createdAt(prettyTime(workout.getCreatedAt()))
                .exerciseDTO(exerciseDTOs)
                .build();
    }

    public WorkoutSetDTO toSetDTO(WorkoutSet s) {
        return WorkoutSetDTO.builder()
                .id(s.getId())
                .setNumber(s.getSetNumber())
                .weight(s.getWeight())
                .reps(s.getReps())
                .rpe(s.getRpe())
                .build();
    }

    public WorkoutExerciseDTO toExerciseDTO(WorkoutExercise we){
        List<WorkoutSetDTO> setDTOs = we.getSets().stream()
                        .filter(s -> s != null)
                        .map(s -> toSetDTO(s))
                        .collect(Collectors.toList());

        String exerciseName = Optional.ofNullable(we.getExercise())
                .map(e -> e.getName())
                .orElse(null);

        return WorkoutExerciseDTO.builder()
                .id(we.getId())
                .orderNum(we.getOrderNum())
                .exerciseName(exerciseName)
                .set(setDTOs)
                .build();
    } 

    public WorkoutDTO toSimpleWorkoutDTO(Workout workout){
        WorkoutDTO dto = new WorkoutDTO();
        dto.setCreatedAt(prettyTime(workout.getCreatedAt()));
        dto.setStatus(workout.getStatus());
        dto.setId(workout.getId());
        return dto;
    }
}