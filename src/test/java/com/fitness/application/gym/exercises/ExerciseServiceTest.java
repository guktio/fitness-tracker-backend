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
import com.fitness.application.gym.exercises.dto.CreateExerciseDTO;
import com.fitness.application.gym.exercises.dto.ExerciseDTO;
import com.fitness.application.gym.exercises.dto.muscles.MuscleDTO;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.exercises.entity.Muscle;
import com.fitness.application.gym.exercises.repository.ExerciseRepository;
import com.fitness.application.users.entity.User;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseMapper exerciseMapper;

    @InjectMocks
    private ExerciseService exerciseService;

    private Exercise exercise;
    private ExerciseDTO exerciseDTO;
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

        exerciseDTO = ExerciseDTO.builder()
                .id(1L)
                .name("Bench Press")
                .build();
    }

    @Test
    void createExercise_shouldSaveAndReturnDto() {
        CreateExerciseDTO createDTO = CreateExerciseDTO.builder()
                .name("Bench Press")
                .build();

        when(exerciseMapper.toEntity(createDTO)).thenReturn(exercise);
        when(exerciseRepository.save(exercise)).thenReturn(exercise);
        when(exerciseMapper.toDTO(exercise)).thenReturn(exerciseDTO);

        ExerciseDTO result = exerciseService.createExercise(createDTO, user);

        assertNotNull(result);
        assertEquals("Bench Press", result.getName());
        assertEquals(user, exercise.getCreatedBy());
        verify(exerciseRepository).save(exercise);
    }

    @Test
    void getExerciseById_whenFound_shouldReturnDto() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(exerciseMapper.toDTO(exercise)).thenReturn(exerciseDTO);

        ExerciseDTO result = exerciseService.getExerciseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getExerciseById_whenNotFound_shouldThrowRuntimeException() {
        when(exerciseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exerciseService.getExerciseById(999L));
    }

    @Test
    void getExerciseOrThrow_whenFound_shouldReturnEntity() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        Exercise result = exerciseService.getExerciseOrThrow(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getExerciseOrThrow_whenNotFound_shouldThrowRuntimeException() {
        when(exerciseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exerciseService.getExerciseOrThrow(999L));
    }

    @Test
    void getAllExercises_shouldReturnPageDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercise> page = new PageImpl<>(List.of(exercise), pageable, 1);

        when(exerciseRepository.findAll(pageable)).thenReturn(page);
        when(exerciseMapper.toDTO(exercise)).thenReturn(exerciseDTO);

        PageDTO<ExerciseDTO> result = exerciseService.getAllExercises(pageable);

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
        Map<Muscle.Category, List<MuscleDTO>> musclesMap = Map.of(
                Muscle.Category.CHEST, List.of()
        );
        when(exerciseMapper.getGroupedMuscles()).thenReturn(musclesMap);

        Map<Muscle.Category, List<MuscleDTO>> result = exerciseService.getGroupedMuscles();

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

        PageDTO<ExerciseDTO> result = exerciseService.getExerciseByFilter(
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

        PageDTO<ExerciseDTO> result = exerciseService.getExerciseByFilter(null, pageable, user.getUuid());

        assertNotNull(result);
        assertEquals(1, result.content().size());
    }
}

