package com.fitness.application.gym.exercises;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.application.base.DTO.PageDTO;
import com.fitness.application.gym.exercises.DTO.CreateExerciseDTO;
import com.fitness.application.gym.exercises.DTO.ExerciseDTO;
import com.fitness.application.gym.exercises.DTO.muscles.MuscleDTO;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.exercises.entity.Muscle;
import com.fitness.application.gym.exercises.repository.ExerciseRepository;
import com.fitness.application.users.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final ExerciseMapper exerciseMapper;

    @Transactional
    public ExerciseDTO createExercise(CreateExerciseDTO dto, User user) {
        log.debug("createExercise with DTO: {} & User: {}", dto.toString(), user.toString());
        Exercise exercise = exerciseMapper.toEntity(dto);
        exercise.setCreatedBy(user);
        Exercise savedExercise = exerciseRepository.save(exercise);
        return exerciseMapper.toDTO(savedExercise);
    }

    @Transactional(readOnly = true)
    public ExerciseDTO getExerciseById(Long id) {
        log.debug("getExerciseById with id: {} & Locale: {}", id);
        return exerciseMapper.toDTO(
            exerciseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Exercise not found"))
        );
    }

    public Exercise getExerciseEntityById(Long id) {
        log.debug("getExerciseEntityById with id: {}",id);
        return exerciseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Exercise not found"));
    }

    public PageDTO<ExerciseDTO> getAllExercises(Pageable pageable) {
        log.debug("getAllExercises with pageable: {}", pageable.toString());
        Page<Exercise> page = exerciseRepository.findAll(pageable);
        return new PageDTO<ExerciseDTO>(
            page.getContent().stream().map(exerciseMapper::toDTO).toList(), 
            page.getPageable().getPageNumber(), 
            page.getTotalPages()
        );
    }

    public Map<Muscle.Category,List<MuscleDTO>> getGroupedMuscles() {
        log.debug("getGroupedMuscles");
        return exerciseMapper.getGroupedMuscles();
    }

    public void deleteExercise(Long id) {
        log.debug("deleteExercise with id: {}", id);
        exerciseRepository.deleteById(id);
    }

    public PageDTO<ExerciseDTO> getExerciseByMuscle(String muscle, Pageable pageable){
        Page<Exercise> page = exerciseRepository.findByMuscle(Muscle.valueOf(muscle),pageable);
        return new PageDTO<ExerciseDTO>(
            page.getContent().stream().map(exerciseMapper::toDTO).toList(), 
            page.getPageable().getPageNumber(), 
            page.getTotalPages()
        );
    }
}
