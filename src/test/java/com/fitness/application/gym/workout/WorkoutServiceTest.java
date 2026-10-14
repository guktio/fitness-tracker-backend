package com.fitness.application.gym.workout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.fitness.application.base.dto.PageDTO;
import com.fitness.application.base.dto.SliceDTO;
import com.fitness.application.gym.exercises.ExerciseService;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.plan.PlanService;
import com.fitness.application.gym.plan.entity.PlanExercise;
import com.fitness.application.gym.plan.entity.WorkoutPlan;
import com.fitness.application.gym.set.SetMapper;
import com.fitness.application.gym.set.SetRepository;
import com.fitness.application.gym.set.SetsDTO;
import com.fitness.application.gym.set.WorkoutSet;
import com.fitness.application.gym.workout.entity.Workout;
import com.fitness.application.gym.workout.entity.WorkoutExercise;
import com.fitness.application.gym.workout.repository.WorkoutExerciseRepository;
import com.fitness.application.gym.workout.repository.WorkoutRepository;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Mock
    private SetRepository setRepository;

    @Mock
    private PlanService planService;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private WorkoutMapper workoutMapper;

    @Mock
    private SetMapper setMapper;

    @InjectMocks
    private WorkoutService workoutService;

    private User user;
    private User otherUser;
    private Workout workout;
    private WorkoutsDTO.Workout workoutDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .uuid(UUID.randomUUID())
                .email("user@example.com")
                .build();

        otherUser = User.builder()
                .uuid(UUID.randomUUID())
                .email("other@example.com")
                .build();

        workout = Workout.builder()
                .id(1L)
                .status(Workout.Status.IN_PROGRESS)
                .createdBy(user)
                .exercises(new ArrayList<>())
                .build();

        workoutDTO = WorkoutsDTO.Workout.builder()
                .id(1L)
                .status(Workout.Status.IN_PROGRESS)
                .build();
    }

    @Test
    void getWorkoutSlice_shouldReturnSliceDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Workout> page = new PageImpl<>(List.of(workout), pageable, 1);

        when(workoutRepository.findAllByCreatedByUuid(user.getUuid(), pageable)).thenReturn(page);
        when(workoutMapper.toSimpleWorkoutDTO(workout)).thenReturn(workoutDTO);

        SliceDTO<WorkoutsDTO.Workout> result = workoutService.getWorkoutSlice(user.getUuid(), pageable);

        assertNotNull(result);
        assertEquals(1, result.items().size());
        assertEquals(0, result.pageNumber());
    }

    @Test
    void getAllWorkouts_whenUserIsValid_shouldReturnPageDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Workout> page = new PageImpl<>(List.of(workout), pageable, 1);

        when(workoutRepository.findAllByCreatedByUuid(user.getUuid(), pageable)).thenReturn(page);
        when(workoutMapper.toSimpleWorkoutDTO(workout)).thenReturn(workoutDTO);

        PageDTO<WorkoutsDTO.Workout> result = workoutService.getAllWorkouts(user, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(0, result.page());
        assertEquals(1, result.totalPages());
    }

    @Test
    void startWorkout_shouldCreateAndReturnWorkout() {
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);
        when(workoutMapper.toSimpleWorkoutDTO(workout)).thenReturn(workoutDTO);

        WorkoutsDTO.Workout result = workoutService.startWorkout(user);

        assertNotNull(result);
        assertEquals(Workout.Status.IN_PROGRESS, result.status());
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void completeWorkout_whenAuthor_shouldCompleteWorkout() {
        when(workoutRepository.findWorkoutById(1L)).thenReturn(Optional.of(workout));
        when(workoutRepository.save(workout)).thenReturn(workout);

        WorkoutsDTO.Workout completedDTO = WorkoutsDTO.Workout.builder()
                .id(1L)
                .status(Workout.Status.COMPLETED)
                .build();
        when(workoutMapper.toSimpleWorkoutDTO(workout)).thenReturn(completedDTO);

        WorkoutsDTO.Workout result = workoutService.completeWorkout(1L, user);

        assertNotNull(result);
        assertEquals(Workout.Status.COMPLETED, workout.getStatus());
        verify(workoutRepository).save(workout);
    }

    @Test
    void completeWorkout_whenNotAuthor_shouldThrowRuntimeException() {
        when(workoutRepository.findWorkoutById(1L)).thenReturn(Optional.of(workout));

        assertThrows(RuntimeException.class, () -> workoutService.completeWorkout(1L, otherUser));
    }

    @Test
    void completeWorkout_whenWorkoutNotFound_shouldThrowEntityNotFoundException() {
        when(workoutRepository.findWorkoutById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> workoutService.completeWorkout(999L, user));
    }

    @Test
    void addExerciseToWorkout_whenAuthor_shouldAddExercise() {
        Exercise exercise = Exercise.builder().id(5L).name("Squat").build();
        WorkoutsDTO.AddExercise addDTO = new WorkoutsDTO.AddExercise(1);
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .id(10L)
                .orderNum(1)
                .workout(workout)
                .exercise(exercise)
                .createdBy(user)
                .build();
        WorkoutsDTO.WorkoutExercise weDTO = WorkoutsDTO.WorkoutExercise.builder()
                .id(10L)
                .orderNum(1)
                .exerciseName("Squat")
                .build();

        when(workoutRepository.findWorkoutById(1L)).thenReturn(Optional.of(workout));
        when(exerciseService.getExerciseOrThrow(5L)).thenReturn(exercise);
        when(workoutExerciseRepository.saveAndFlush(any(WorkoutExercise.class))).thenReturn(workoutExercise);
        when(workoutMapper.toWorkoutExerciseDTO(workoutExercise)).thenReturn(weDTO);

        WorkoutsDTO.WorkoutExercise result = workoutService.addExerciseToWorkout(1L, 5L, addDTO, user);

        assertNotNull(result);
        assertEquals(10L, result.id());
        verify(workoutExerciseRepository).saveAndFlush(any(WorkoutExercise.class));
    }

    @Test
    void addExerciseToWorkout_whenNotAuthor_shouldThrowRuntimeException() {
        WorkoutsDTO.AddExercise addDTO = new WorkoutsDTO.AddExercise(1);
        when(workoutRepository.findWorkoutById(1L)).thenReturn(Optional.of(workout));

        assertThrows(RuntimeException.class, () -> workoutService.addExerciseToWorkout(1L, 5L, addDTO, otherUser));
    }

    @Test
    void addSetToWorkoutExercise_whenAuthor_shouldAddSet() {
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .id(10L)
                .workout(workout)
                .createdBy(user)
                .sets(new ArrayList<>())
                .build();

        SetsDTO.Create setToAdd = SetsDTO.Create.builder()
                .weight(100.0)
                .setNumber(1)
                .reps(10)
                .rpe(8.0)
                .build();

        WorkoutSet savedSet = WorkoutSet.builder()
                .id(20L)
                .weight(100.0)
                .setNumber(1)
                .reps(10)
                .rpe(8.0)
                .createdBy(user)
                .workoutExercise(workoutExercise)
                .build();

        SetsDTO.Set setDTO = SetsDTO.Set.builder()
                .id(20L)
                .weight(100.0)
                .setNumber(1)
                .reps(10)
                .rpe(8.0)
                .build();

        when(workoutExerciseRepository.findById(10L)).thenReturn(Optional.of(workoutExercise));
        when(setRepository.saveAndFlush(any(WorkoutSet.class))).thenReturn(savedSet);
        when(setMapper.toSetDTO(savedSet)).thenReturn(setDTO);

        SetsDTO.Set result = workoutService.addSetToWorkoutExercise(10L, setToAdd, user);

        assertNotNull(result);
        assertEquals(20L, result.id());
        verify(setRepository).saveAndFlush(any(WorkoutSet.class));
    }

    @Test
    void addSetToWorkoutExercise_whenNotAuthor_shouldThrowRuntimeException() {
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .id(10L)
                .workout(workout)
                .createdBy(user)
                .sets(new ArrayList<>())
                .build();

        SetsDTO.Create setToAdd = SetsDTO.Create.builder()
                .weight(100.0)
                .setNumber(1)
                .reps(10)
                .rpe(8.0)
                .build();
        when(workoutExerciseRepository.findById(10L)).thenReturn(Optional.of(workoutExercise));

        assertThrows(RuntimeException.class, () -> workoutService.addSetToWorkoutExercise(10L, setToAdd, otherUser));
    }

    @Test
    void deleteExerciseFromWorkout_whenAuthor_shouldRemoveExercise() {
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .id(10L)
                .workout(workout)
                .createdBy(user)
                .build();
        workout.addExercise(workoutExercise);

        when(workoutRepository.findWorkoutById(1L)).thenReturn(Optional.of(workout));
        when(workoutExerciseRepository.findByWorkoutIdAndExerciseId(1L, 5L)).thenReturn(Optional.of(workoutExercise));

        workoutService.deleteExerciseFromWorkout(1L, 5L, user);

        verify(workoutRepository).saveAndFlush(workout);
    }

    @Test
    void deleteExerciseFromWorkout_whenNotAuthor_shouldThrowRuntimeException() {
        when(workoutRepository.findWorkoutById(1L)).thenReturn(Optional.of(workout));

        assertThrows(RuntimeException.class, () -> workoutService.deleteExerciseFromWorkout(1L, 5L, otherUser));
    }

    @Test
    void deleteSetFromExercise_shouldRemoveSet() {
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .id(10L)
                .sets(new ArrayList<>())
                .build();
        WorkoutSet set = WorkoutSet.builder().id(20L).build();
        workoutExercise.addSet(set);

        when(workoutExerciseRepository.findById(10L)).thenReturn(Optional.of(workoutExercise));
        when(setRepository.findById(20L)).thenReturn(Optional.of(set));

        workoutService.deleteSetFromExercise(10L, 20L, user);

        verify(workoutExerciseRepository).saveAndFlush(workoutExercise);
    }

    @Test
    void getWorkoutInfo_shouldReturnWorkoutInfo() {
        WorkoutsDTO.Info info = WorkoutsDTO.Info.builder().id(1L).build();
        when(workoutRepository.findWorkoutById(1L)).thenReturn(Optional.of(workout));
        when(workoutMapper.toWorkoutInfo(workout)).thenReturn(info);

        WorkoutsDTO.Info result = workoutService.getWorkoutInfo(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void createWorkoutByPlan_shouldPopulateExercisesFromPlan() {
        Exercise exercise = Exercise.builder().id(5L).name("Squat").build();
        PlanExercise planExercise = PlanExercise.builder()
                .id(100L)
                .orderNum(1)
                .exercise(exercise)
                .build();

        WorkoutPlan plan = WorkoutPlan.builder()
                .id(50L)
                .exercises(List.of(planExercise))
                .build();

        WorkoutsDTO.Info info = WorkoutsDTO.Info.builder().id(1L).build();

        when(planService.getWorkoutPlanOrThrow(50L)).thenReturn(plan);
        when(workoutRepository.findWorkoutById(1L)).thenReturn(Optional.of(workout));
        when(workoutRepository.save(workout)).thenReturn(workout);
        when(workoutMapper.toWorkoutInfo(workout)).thenReturn(info);

        WorkoutsDTO.Info result = workoutService.createWorkoutByPlan(50L, 1L, user);

        assertNotNull(result);
        assertEquals(1, workout.getExercises().size());
        verify(workoutRepository).save(workout);
    }
}
