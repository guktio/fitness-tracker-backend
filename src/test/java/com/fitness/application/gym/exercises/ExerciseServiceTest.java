package com.fitness.application.gym.exercises;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
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

import com.fitness.application.base.dto.PageDTO;
import com.fitness.application.gym.exercises.dto.ExercisesDTO;
import com.fitness.application.gym.exercises.dto.MusclesDTO;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.exercises.entity.Muscle;
import com.fitness.application.gym.exercises.repository.ExerciseRepository;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseMapper exerciseMapper;

    @InjectMocks
    private ExerciseService exerciseService;

    private Exercise exercise;
    private ExercisesDTO.Exercise exerciseDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .uuid(UUID.randomUUID())
                .email("user@example.com")
                .build();

        exercise = Exercise.builder()
                .id(1L)
                .name("Bench Press")
                .createdBy(user)
                .build();

        exerciseDTO = ExercisesDTO.Exercise.builder()
                .id(1L)
                .name("Bench Press")
                .description("Sample description")
                .build();
    }

    @Test
    void createExercise_shouldSaveAndReturnDto() {
        ExercisesDTO.CreateExercise createDTO = new ExercisesDTO.CreateExercise("Bench Press", "Sample description", null);

        when(exerciseMapper.toEntity(createDTO)).thenReturn(exercise);
        when(exerciseRepository.save(exercise)).thenReturn(exercise);
        when(exerciseMapper.toDTO(exercise)).thenReturn(exerciseDTO);

        ExercisesDTO.Exercise result = exerciseService.createExercise(createDTO, user);

        assertNotNull(result);
        assertEquals("Bench Press", result.name());
        assertEquals(user, exercise.getCreatedBy());
        verify(exerciseRepository).save(exercise);
    }

    @Test
    void getExerciseById_whenFound_shouldReturnDto() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(exerciseMapper.toDTO(exercise)).thenReturn(exerciseDTO);

        ExercisesDTO.Exercise result = exerciseService.getExerciseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getExerciseById_whenNotFound_shouldThrowEntityNotFoundException() {
        when(exerciseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> exerciseService.getExerciseById(999L));
    }

    @Test
    void getExerciseOrThrow_whenFound_shouldReturnEntity() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        Exercise result = exerciseService.getExerciseOrThrow(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getExerciseOrThrow_whenNotFound_shouldThrowEntityNotFoundException() {
        when(exerciseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> exerciseService.getExerciseOrThrow(999L));
    }

    @Test
    void getAllExercises_shouldReturnPageDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercise> page = new PageImpl<>(List.of(exercise), pageable, 1);

        when(exerciseRepository.findAll(pageable)).thenReturn(page);
        when(exerciseMapper.toDTO(exercise)).thenReturn(exerciseDTO);

        PageDTO<ExercisesDTO.Exercise> result = exerciseService.getAllExercises(pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(0, result.page());
        assertEquals(1, result.totalPages());
    }

    @Test
    void deleteExercise_shouldCallRepositoryDeleteById() {
        exerciseService.deleteExercise(1L);

        verify(exerciseRepository).deleteById(1L);
    }

    @Test
    void getGroupedMuscles_shouldReturnMap() {
        Map<Muscle.Category, List<MusclesDTO.Muscle>> musclesMap = Map.of(
                Muscle.Category.CHEST, List.of(MusclesDTO.Muscle.builder().code("CHEST").build())
        );
        when(exerciseMapper.getGroupedMuscles()).thenReturn(musclesMap);

        Map<Muscle.Category, List<MusclesDTO.Muscle>> result = exerciseService.getGroupedMuscles();

        assertNotNull(result);
        assertEquals(musclesMap, result);
    }

    @Test
    void getExerciseByFilter_withMuscleAndUuid_shouldReturnFilteredPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercise> page = new PageImpl<>(List.of(exercise), pageable, 1);

        when(exerciseRepository.findByFilter(eq(Muscle.PECTORALIS_MAJOR_CLAVICULAR), eq(user.getUuid()), eq(pageable)))
                .thenReturn(page);
        when(exerciseMapper.toDTO(exercise)).thenReturn(exerciseDTO);

        PageDTO<ExercisesDTO.Exercise> result = exerciseService.getExerciseByFilter(
                "PECTORALIS_MAJOR_CLAVICULAR", pageable, user.getUuid());

        assertNotNull(result);
        assertEquals(1, result.content().size());
    }

    @Test
    void getExerciseByFilter_withNullMuscle_shouldPassNullMuscleToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercise> page = new PageImpl<>(List.of(exercise), pageable, 1);

        when(exerciseRepository.findByFilter(eq(null), eq(user.getUuid()), eq(pageable)))
                .thenReturn(page);
        when(exerciseMapper.toDTO(exercise)).thenReturn(exerciseDTO);

        PageDTO<ExercisesDTO.Exercise> result = exerciseService.getExerciseByFilter(null, pageable, user.getUuid());

        assertNotNull(result);
        assertEquals(1, result.content().size());
    }
}
