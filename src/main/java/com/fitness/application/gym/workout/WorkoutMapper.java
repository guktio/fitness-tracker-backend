package com.fitness.application.gym.workout;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fitness.application.gym.set.SetMapper;
import com.fitness.application.gym.set.SetsDTO;
import com.fitness.application.gym.workout.entity.Workout;
import com.fitness.application.gym.workout.entity.WorkoutExercise;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class WorkoutMapper {

    private final SetMapper setMapper;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

    private String prettyTime(Instant time){
        return DATE_TIME_FORMATTER.format(time.atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    public WorkoutsDTO.Info toWorkoutInfo(Workout workout) {
        List<WorkoutsDTO.WorkoutExercise> exerciseDTOs = workout.getExercises().stream()
                .map(we -> toWorkoutExerciseDTO(we))
                .collect(Collectors.toList());

        return WorkoutsDTO.Info.builder()
                .id(workout.getId())
                .status(workout.getStatus())
                .createdAt(prettyTime(workout.getCreatedAt()))
                .exercises(exerciseDTOs)
                .build();
    }

    public WorkoutsDTO.WorkoutExercise toWorkoutExerciseDTO(WorkoutExercise we){
        List<SetsDTO.Set> setDTOs = we.getSets().stream()
                        .filter(s -> s != null)
                        .map(s -> setMapper.toSetDTO(s))
                        .collect(Collectors.toList());

        String exerciseName = Optional.ofNullable(we.getExercise())
                .map(e -> e.getName())
                .orElse(null);

        return WorkoutsDTO.WorkoutExercise.builder()
                .id(we.getId())
                .orderNum(we.getOrderNum())
                .exerciseName(exerciseName)
                .sets(setDTOs)
                .build();
    } 

    public WorkoutsDTO.Workout toSimpleWorkoutDTO(Workout workout){
        return WorkoutsDTO.Workout.builder()
                            .createdAt(prettyTime(workout.getCreatedAt()))
                            .status(workout.getStatus())
                            .id(workout.getId())
                            .build();
    }
}