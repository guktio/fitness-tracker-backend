package com.fitness.application.gym.workout;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.application.gym.exercises.ExerciseService;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.workout.DTO.ExerciseAddDTO;
import com.fitness.application.gym.workout.DTO.WorkoutDTO;
import com.fitness.application.gym.workout.DTO.SliceDTO;
import com.fitness.application.gym.workout.DTO.WorkoutExerciseDTO;
import com.fitness.application.gym.workout.DTO.WorkoutInfo;
import com.fitness.application.gym.workout.DTO.WorkoutSetDTO;
import com.fitness.application.gym.workout.entity.Workout;
import com.fitness.application.gym.workout.entity.WorkoutExercise;
import com.fitness.application.gym.workout.entity.WorkoutSet;
import com.fitness.application.gym.workout.repository.WorkoutExerciseRepository;
import com.fitness.application.gym.workout.repository.WorkoutRepository;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    private final WorkoutExerciseRepository workoutExerciseRepository;

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

    @Transactional
    public WorkoutExerciseDTO addExerciseToWorkout(Long id, ExerciseAddDTO dto, User user) {
        Workout workout = getWorkoutOrThrow(id);
        Exercise exercise = exerciseService.getExerciseEntityById(dto.exerciseId());
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                            .orderNum(dto.orderNum())
                            .workout(workout)
                            .exercise(exercise)
                            .createdBy(user)
                            .build();
        workout.addExercise(workoutExercise);
        workoutRepository.saveAndFlush(workout);
        return workoutMapper.toExerciseDTO(workoutExercise);
    }

    public SliceDTO<WorkoutDTO> getWorkoutSlice(UUID uuid, Pageable pageable){
        Slice<Workout> workout = workoutRepository.findAllByCreatedBy(uuid, pageable);
        return new SliceDTO<WorkoutDTO>(
            workout.getContent()
                .stream()
                    .map(workoutMapper::toSimpleWorkoutDTO)
                    .toList(),
            !workout.isLast(),
            workout.getNumber()
        );
    }

    public WorkoutInfo getWorkoutInfo(Long wId){
        Workout workout = workoutRepository.findById(wId)
            .orElseThrow(() -> new IllegalArgumentException("Workout not found: " + wId));
        return workoutMapper.toWorkoutInfo(workout);
    }

    @Transactional
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
        workoutExercise.addSet(set);
        workoutExerciseRepository.saveAndFlush(workoutExercise);
        return workoutMapper.toSetDTO(set);
    }

    public void deleteExerciseFromWorkout(Long wId, Long exId){
        WorkoutExercise workoutExercise = workoutExerciseRepository.findByWorkoutIdAndExerciseId(wId,exId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise with id:" + exId + " and workoutId:" + wId+ " not found"));
        workoutExerciseRepository.delete(workoutExercise);
    }
}