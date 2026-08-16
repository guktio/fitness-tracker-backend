package com.fitness.application.gym.workout;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.fitness.application.gym.exercises.ExerciseService;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.workout.DTO.ExerciseAddDTO;
import com.fitness.application.gym.workout.DTO.SimpleWorkoutDTO;
import com.fitness.application.gym.workout.DTO.SliceDTO;
import com.fitness.application.gym.workout.DTO.WorkoutExerciseDTO;
import com.fitness.application.gym.workout.DTO.WorkoutInfo;
import com.fitness.application.gym.workout.DTO.WorkoutSetDTO;
import com.fitness.application.gym.workout.entity.Workout;
import com.fitness.application.gym.workout.entity.WorkoutExercise;
import com.fitness.application.gym.workout.entity.WorkoutSet;
import com.fitness.application.gym.workout.repository.WorkoutExerciseRepository;
import com.fitness.application.gym.workout.repository.WorkoutRepository;
import com.fitness.application.gym.workout.repository.WorkoutSetRepository;
import com.fitness.application.users.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutSetRepository workoutSetRepository;

    private final ExerciseService exerciseService;

    private final WorkoutMapper workoutMapper;

    public Workout startWorkout(User user) {
        Workout workout = Workout.builder()
                                .status(Workout.Status.IN_PROGRESS)
                                .createdBy(user)
                                .build();
        return workoutRepository.save(workout);
    }

    public Workout completeWorkout(Long workoutId) {
        Workout workout = getWorkoutOrThrow(workoutId);
        workout.setStatus(Workout.Status.COMPLETED);
        return workoutRepository.save(workout);
    }

    private Workout getWorkoutOrThrow(Long workoutId) {
        return workoutRepository.findWorkoutById(workoutId)
            .orElseThrow(() -> new RuntimeException("Workout not found"));
    }

    public WorkoutExerciseDTO addExerciseToWorkout(Long id, ExerciseAddDTO dto, User user) {
        Workout workout = getWorkoutOrThrow(id);
        Exercise exercise = exerciseService.getExerciseEntityById(dto.exerciseId());
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                                        .orderNum(dto.orderNum())
                                        .workout(workout)
                                        .exercise(exercise)
                                        .createdBy(user)
                                        .build();
        return workoutMapper.toExerciseDTO(workoutExerciseRepository.save(workoutExercise), null);
    }

    public SliceDTO<SimpleWorkoutDTO> getWorkoutSlice(UUID uuid, Pageable pageable){
        Slice<Workout> workout = workoutRepository.findAllByCreatedBy(uuid, pageable);
        return new SliceDTO<SimpleWorkoutDTO>(
            workout.getContent()
                .stream()
                    .map(workoutMapper::toSimpleWorkoutDTO)
                    .toList(),
            !workout.isLast(),
            workout.getNumber()
        );
    }

    public WorkoutInfo getWorkoutInfo(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found: " + workoutId));

        log.info("Fetching info for workout ID: {} [Status: {}]", workoutId, workout.getStatus());

        List<WorkoutExercise> exercises =
                workoutExerciseRepository.findByWorkoutIdOrderByOrderNumAsc(workoutId);

        List<String> exerciseNames = exercises.stream()
                .map(e -> e.getExercise().getName())
                .toList();

        List<Long> exerciseIds = exercises.stream()
                .map(e -> e.getId())
                .toList();

        log.info("Found {} exercises: {} (IDs: {})", exercises.size(), exerciseNames, exerciseIds);

        List<WorkoutSet> allSets = exerciseIds.isEmpty()
                ? List.of()
                : workoutSetRepository.findByWorkoutExerciseIdInOrderBySetNumberAsc(exerciseIds);

        Map<Long, List<WorkoutSet>> setsByExerciseId = allSets.stream()
                .collect(Collectors.groupingBy(s -> s.getWorkoutExercise().getId()));

        if (allSets.isEmpty()) {
            log.info("No sets found for this workout.");
        } else {
            String setsSummary = setsByExerciseId.entrySet().stream()
                    .map(e -> "Exercise ID " + e.getKey() + " -> " + e.getValue().size() + " sets")
                    .collect(Collectors.joining(", "));
            
            log.info("Total sets: {}. Summary: [{}]", allSets.size(), setsSummary);
        }

        return workoutMapper.toWorkoutInfo(workout, exercises, setsByExerciseId);
    }


    public WorkoutSetDTO addSetToWorkoutExercise(Long workoutExerciseId, WorkoutSet dto, User user) {
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(workoutExerciseId)
            .orElseThrow(() -> new RuntimeException("WorkoutExercise not found"));

        WorkoutSet set = WorkoutSet.builder()
                                .workoutExercise(workoutExercise)
                                .weight(dto.getWeight())
                                .setNumber(dto.getSetNumber())
                                .reps(dto.getReps())
                                .rpe(dto.getRpe())
                                .createdBy(user)
                                .build();
        return workoutMapper.toSetDTO(workoutSetRepository.save(set));
    }
}