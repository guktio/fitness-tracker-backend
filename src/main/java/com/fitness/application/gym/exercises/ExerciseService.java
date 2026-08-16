package com.fitness.application.gym.exercises;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.application.gym.exercises.DTO.CreateExerciseDTO;
import com.fitness.application.gym.exercises.DTO.ExerciseDTO;
import com.fitness.application.gym.exercises.DTO.PageDTO;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.exercises.entity.Muscle;
import com.fitness.application.gym.exercises.repository.ExerciseRepository;
import com.fitness.application.users.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final ExerciseMapper exerciseMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional
    public ExerciseDTO createExercise(CreateExerciseDTO dto, User user) {
        logger.debug("createExercise with DTO: {} & User: {}", dto.toString(), user.toString());
        Exercise exercise = exerciseMapper.toEntity(dto);
        exercise.setCreatedBy(user);
        Exercise savedExercise = exerciseRepository.save(exercise);
        return exerciseMapper.toDTO(savedExercise);
    }

    @Transactional(readOnly = true)
    public ExerciseDTO getExerciseById(Long id, Locale locale) {
        logger.debug("getExerciseById with id: {} & Locale: {}", id, locale);
        return exerciseMapper.toDTO(
            exerciseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Exercise not found"))
        );
    }

    public Exercise getExerciseEntityById(Long id) {
        logger.debug("getExerciseEntityById with id: {}",id);
        return exerciseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Exercise not found"));
    }

    public PageDTO<ExerciseDTO> getAllExercises(Pageable pageable) {
        logger.debug("getAllExercises with pageable: {}", pageable.toString());
        Page<Exercise> page = exerciseRepository.findAll(pageable);
        return new PageDTO<ExerciseDTO>(
            page.getContent().stream().map(exerciseMapper::toDTO).toList(), 
            page.getPageable().getPageNumber(), 
            page.getTotalPages()
        );
    }

    public Map<Muscle.Category,List<Muscle>> getGroupedMuscles(Locale locale) {
        logger.debug("getGroupedMuscles wiht Locale: {}", locale);
        return exerciseMapper.getGroupedMuscles(locale);
    }

    public void deleteExercise(Long id) {
        logger.debug("deleteExercise with id: {}", id);
        exerciseRepository.deleteById(id);
    }
}
