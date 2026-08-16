package com.fitness.application.gym.workout;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fitness.application.gym.workout.DTO.SimpleWorkoutDTO;
import com.fitness.application.gym.workout.DTO.WorkoutExerciseDTO;
import com.fitness.application.gym.workout.DTO.WorkoutInfo;
import com.fitness.application.gym.workout.DTO.WorkoutSetDTO;
import com.fitness.application.gym.workout.entity.Workout;
import com.fitness.application.gym.workout.entity.WorkoutExercise;
import com.fitness.application.gym.workout.entity.WorkoutSet;

@Component
public class WorkoutMapper {

    public SimpleWorkoutDTO toSimpleWorkoutDTO (Workout workout){
        LocalDateTime createdAt = workout.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
        SimpleWorkoutDTO dto = new SimpleWorkoutDTO();
        dto.setCreatedAt(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy").format(createdAt));
        dto.setStatus(workout.getStatus());
        dto.setId(workout.getId());
        return dto;
    }

    public WorkoutInfo toWorkoutInfo(Workout workout,
                                      List<WorkoutExercise> exercises,
                                      Map<Long, List<WorkoutSet>> setsByExerciseId) {

        List<WorkoutExerciseDTO> exerciseDTOs = exercises.stream()
                .map(we -> toExerciseDTO(we, setsByExerciseId.getOrDefault(we.getId(), List.of())))
                .toList();

        LocalDateTime createdAt = workout.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return WorkoutInfo.builder()
                .workout(workout)
                .createdAt(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy").format(createdAt))
                .exerciseDTO(exerciseDTOs)
                .build();
    }

    public WorkoutExerciseDTO toExerciseDTO(WorkoutExercise we, List<WorkoutSet> sets) {
        if (we == null) {
            return null;
        }

        List<WorkoutSetDTO> setDTOs = Optional.ofNullable(sets)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(s -> s != null)
                .map(this::toSetDTO)
                .toList();

        String exerciseName = Optional.ofNullable(we.getExercise())
                .map(e -> e.getName())
                .orElse(null);

        return WorkoutExerciseDTO.builder()
                .id(we.getId())
                .orderNum(we.getOrderNum())
                .exerciseName(exerciseName)
                .setDTO(setDTOs)
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
}