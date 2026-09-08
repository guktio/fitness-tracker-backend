package com.fitness.application.gym.workout;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.application.gym.exercises.ExerciseService;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.workout.DTO.ExerciseAddDTO;
import com.fitness.application.gym.workout.DTO.SliceDTO;
import com.fitness.application.gym.workout.DTO.WorkoutDTO;
import com.fitness.application.gym.workout.DTO.WorkoutExerciseDTO;
import com.fitness.application.gym.workout.DTO.WorkoutInfo;
import com.fitness.application.gym.workout.DTO.WorkoutPlanDTO;
import com.fitness.application.gym.workout.DTO.WorkoutSetDTO;
import com.fitness.application.gym.workout.entity.PlanExercise;
import com.fitness.application.gym.workout.entity.Workout;
import com.fitness.application.gym.workout.entity.WorkoutExercise;
import com.fitness.application.gym.workout.entity.WorkoutPlan;
import com.fitness.application.gym.workout.entity.WorkoutSet;
import com.fitness.application.gym.workout.repository.WorkoutExerciseRepository;
import com.fitness.application.gym.workout.repository.WorkoutPlanExerciseRepository;
import com.fitness.application.gym.workout.repository.WorkoutPlanRepository;
import com.fitness.application.gym.workout.repository.WorkoutRepository;
import com.fitness.application.gym.workout.repository.WorkoutSetRepository;
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

    private final WorkoutSetRepository setRepository;

    private final WorkoutPlanRepository workoutPlanRepository;

    private final WorkoutPlanExerciseRepository workoutPlanExerciseRepository;

    private final ExerciseService exerciseService;

    private final WorkoutMapper workoutMapper;

    @Transactional
    public WorkoutDTO startWorkout(User user) {
        Workout workout = Workout.builder()
                                .status(Workout.Status.IN_PROGRESS)
                                .createdBy(user)
                                .build();
        Workout saved = workoutRepository.save(workout);
        return workoutMapper.toSimpleWorkoutDTO(saved);
    }

    @Transactional
    public WorkoutDTO completeWorkout(Long workoutId, User user) {
        Workout workout = getWorkoutOrThrow(workoutId);
        if (!isAuthor(workout.getCreatedBy().getUuid(), user)) {
            throw new RuntimeException("Cannot stop workout: workout belongs to another user.");
        }
        workout.setStatus(Workout.Status.COMPLETED);
        Workout saved = workoutRepository.save(workout);
        return workoutMapper.toSimpleWorkoutDTO(saved);
    }

    @Transactional
    public WorkoutExerciseDTO addExerciseToWorkout(Long wId, Long exId, ExerciseAddDTO dto, User user) {
        Workout workout = getWorkoutOrThrow(wId);
        if (!isAuthor(workout.getCreatedBy().getUuid(),user)) {
            throw new RuntimeException("Cannot add exercise: workout belongs to another user.");
        }
        
        Exercise exercise = exerciseService.getExerciseOrThrow(exId);
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                            .orderNum(dto.orderNum())
                            .workout(workout)
                            .exercise(exercise)
                            .createdBy(user)
                            .build();
        workout.addExercise(workoutExercise);
        WorkoutExercise saved = workoutExerciseRepository.saveAndFlush(workoutExercise);
        return workoutMapper.toWorkoutExerciseDTO(saved);
    }

    @Transactional
    public WorkoutSetDTO addSetToWorkoutExercise(Long weId, WorkoutSet dto, User user) {
        WorkoutExercise workoutExercise = getWorkoutExerciseByIdOrThrow(weId);
        if (!isAuthor(workoutExercise.getCreatedBy().getUuid(),user)) {
            throw new RuntimeException("Cannot add set: workout belongs to another user.");
        }
        WorkoutSet set = WorkoutSet.builder()
                                .workoutExercise(workoutExercise)
                                .weight(dto.getWeight())
                                .setNumber(dto.getSetNumber())
                                .reps(dto.getReps())
                                .rpe(dto.getRpe())
                                .createdBy(user)
                                .build();
        workoutExercise.addSet(set);
        WorkoutSet savedSet = setRepository.saveAndFlush(set);
        return workoutMapper.toSetDTO(savedSet);
    }

    @Transactional
    public void deleteExerciseFromWorkout(Long wId, Long exId, User user){
        Workout workout = getWorkoutOrThrow(wId);
        if (!isAuthor(workout.getCreatedBy().getUuid(), user)) {
            throw new RuntimeException("Cannot : workout belongs to another user.");
        }
        WorkoutExercise workoutExercise = workoutExerciseRepository.findByWorkoutIdAndExerciseId(wId, exId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise with id:" + exId + " and workoutId:" + wId+ " not found"));
        workout.removeExercise(workoutExercise);
        workoutRepository.saveAndFlush(workout);
    }

    @Transactional
    public void deleteSetFromExercise(Long weId, Long sId, User user){
        WorkoutExercise workoutExercise = getWorkoutExerciseByIdOrThrow(weId);
        WorkoutSet set = setRepository.findById(sId)
                .orElseThrow(() -> new EntityNotFoundException("Set with id:" + sId + " not found"));
        workoutExercise.removeSet(set);
        workoutExerciseRepository.saveAndFlush(workoutExercise);
    }

    public SliceDTO<WorkoutDTO> getWorkoutSlice(UUID uuid, Pageable pageable){
        Slice<Workout> workout = workoutRepository.findAllByCreatedByUuid(uuid, pageable);
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
        Workout workout = getWorkoutOrThrow(wId);
        return workoutMapper.toWorkoutInfo(workout);
    }

    private Workout getWorkoutOrThrow(Long wId) {
        return workoutRepository.findWorkoutById(wId)
            .orElseThrow(() -> new EntityNotFoundException("Workout not found"));
    }

    private WorkoutExercise getWorkoutExerciseByIdOrThrow(Long weId){
        return workoutExerciseRepository.findById(weId)
                .orElseThrow(() -> new EntityNotFoundException("WorkoutExercise with id:" + weId + " not found"));
    }

    private boolean isAuthor(UUID entityAuthorUuid, User currentUserUuid){
        return entityAuthorUuid.equals(currentUserUuid.getUuid());
    }

    private WorkoutPlan getWorkoutPlanOrThrow(Long id){
        return workoutPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("WorkoutPlan with id:"+id+" not found"));
    }

    public WorkoutPlanDTO getWorkoutPlanDTO(Long id){
        return workoutMapper.toWorkoutPlanDTO(getWorkoutPlanOrThrow(id));
    }

    public WorkoutPlanDTO createWorkoutPlan(WorkoutPlan workoutPlan){
        WorkoutPlan saved = workoutPlanRepository.save(workoutPlan);
        return workoutMapper.toWorkoutPlanDTO(saved);
    }

    @Transactional
    public WorkoutPlanDTO addExerciseToPlan(long pid, long eid, int orderNum, User user){
        WorkoutPlan workoutPlan = getWorkoutPlanOrThrow(pid);
        Exercise exercise = exerciseService.getExerciseOrThrow(eid);
        PlanExercise pExercise = PlanExercise.builder()
                                        .orderNum(orderNum)
                                        .exercise(exercise)
                                        .workoutPlan(workoutPlan)
                                        .createdBy(user)
                                        .build();
        workoutPlanExerciseRepository.saveAndFlush(pExercise);
        workoutPlan.getExercises().add(pExercise);
        WorkoutPlan newWorkoutPlan = workoutPlanRepository.save(workoutPlan);
        log.info(newWorkoutPlan.toString());
        return workoutMapper.toWorkoutPlanDTO(newWorkoutPlan);
    }

    @Transactional
    public WorkoutInfo createWorkoutByPlan(long plan_id, long workout_id, User user){
        WorkoutPlan workoutPlan = getWorkoutPlanOrThrow(plan_id);
        Workout workout = getWorkoutOrThrow(workout_id);
        List<PlanExercise> exercises = workoutPlan.getExercises();
        for (PlanExercise exercise : exercises) {
            WorkoutExercise workoutExercise = WorkoutExercise.builder()
                            .orderNum(exercise.getOrderNum())
                            .workout(workout)
                            .exercise(exercise.getExercise())
                            .createdBy(user)
                            .build();
            workout.addExercise(workoutExercise);
        }
        Workout saved = workoutRepository.save(workout);
        return workoutMapper.toWorkoutInfo(saved);
    }
}