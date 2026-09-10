package com.fitness.application.gym.plan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fitness.application.gym.exercises.ExerciseService;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.plan.dto.WorkoutPlanDTO;
import com.fitness.application.gym.plan.entity.PlanExercise;
import com.fitness.application.gym.plan.entity.WorkoutPlan;
import com.fitness.application.gym.plan.repository.PlanExerciseRepository;
import com.fitness.application.gym.plan.repository.PlanRepository;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PlanExerciseRepository planExerciseRepository;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private PlanMapper planMapper;

    @InjectMocks
    private PlanService planService;

    private WorkoutPlan plan;
    private WorkoutPlanDTO planDTO;
    private Exercise exercise;
    private User user;

    @BeforeEach
    void setUp() {
        plan = WorkoutPlan.builder()
                .id(1L)
                .title("Full Body Plan")
                .description("Sample plan")
                .exercises(new ArrayList<>())
                .build();

        planDTO = WorkoutPlanDTO.builder()
                .id(1L)
                .title("Full Body Plan")
                .description("Sample plan")
                .exercises(new ArrayList<>())
                .build();

        exercise = Exercise.builder()
                .id(10L)
                .name("Deadlift")
                .build();

        user = User.builder()
                .username("fitness_user")
                .build();
    }

    @Test
    void getWorkoutPlanOrThrow_whenFound_shouldReturnPlan() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        WorkoutPlan result = planService.getWorkoutPlanOrThrow(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getWorkoutPlanOrThrow_whenNotFound_shouldThrowEntityNotFoundException() {
        when(planRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> planService.getWorkoutPlanOrThrow(999L));
    }

    @Test
    void getWorkoutPlanDTO_whenFound_shouldReturnDto() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(planMapper.toWorkoutPlanDTO(plan)).thenReturn(planDTO);

        WorkoutPlanDTO result = planService.getWorkoutPlanDTO(1L);

        assertNotNull(result);
        assertEquals("Full Body Plan", result.getTitle());
    }

    @Test
    void createWorkoutPlan_shouldSaveAndReturnDto() {
        when(planRepository.save(plan)).thenReturn(plan);
        when(planMapper.toWorkoutPlanDTO(plan)).thenReturn(planDTO);

        WorkoutPlanDTO result = planService.createWorkoutPlan(plan);

        assertNotNull(result);
        assertEquals("Full Body Plan", result.getTitle());
        verify(planRepository).save(plan);
    }

    @Test
    void addExerciseToPlan_shouldAddExerciseAndReturnUpdatedDto() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(exerciseService.getExerciseOrThrow(10L)).thenReturn(exercise);
        when(planRepository.save(plan)).thenReturn(plan);
        when(planMapper.toWorkoutPlanDTO(plan)).thenReturn(planDTO);

        WorkoutPlanDTO result = planService.addExerciseToPlan(1L, 10L, 1, user);

        assertNotNull(result);
        assertEquals(1, plan.getExercises().size());
        verify(planExerciseRepository).saveAndFlush(any(PlanExercise.class));
        verify(planRepository).save(plan);
    }
}

