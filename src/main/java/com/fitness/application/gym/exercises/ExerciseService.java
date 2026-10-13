package com.fitness.application.gym.exercises;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.application.base.dto.PageDTO;
import com.fitness.application.gym.exercises.dto.ExercisesDTO;
import com.fitness.application.gym.exercises.dto.MusclesDTO;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.exercises.entity.Muscle;
import com.fitness.application.gym.exercises.repository.ExerciseRepository;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final ExerciseMapper exerciseMapper;

    @Transactional
    @Cacheable(
        value = "exercise",
        key = "#id"
    )
    public ExercisesDTO.Exercise createExercise(ExercisesDTO.CreateExercise dto, User user) {
        log.debug("createExercise with ExercisesDTO: {} & User: {}", dto.toString(), user.toString());
        Exercise exercise = exerciseMapper.toEntity(dto);
        exercise.setCreatedBy(user);
        Exercise savedExercise = exerciseRepository.save(exercise);
        return exerciseMapper.toDTO(savedExercise);
    }

    @Transactional(readOnly = true)
    @Cacheable(
        value = "exercise",
        key = "#id + ':' + T(org.springframework.context.i18n.LocaleContextHolder).getLocale()"
    )
    public ExercisesDTO.Exercise getExerciseById(Long id) {
        log.debug("getExerciseById with id: {} & Locale: {}", id);
        return exerciseMapper.toDTO(getExerciseOrThrow(id));
    }

    public Exercise getExerciseOrThrow(Long id) {
        log.debug("getExerciseEntityById with id: {}",id);
        return exerciseRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));
    }

    public PageDTO<ExercisesDTO.Exercise> getAllExercises(Pageable pageable) {
        log.debug("getAllExercises with pageable: {}", pageable.toString());
        Page<Exercise> page = exerciseRepository.findAll(pageable);
        return new PageDTO<ExercisesDTO.Exercise>(
            page.getContent().stream().map(exerciseMapper::toDTO).toList(), 
            page.getPageable().getPageNumber(), 
            page.getTotalPages()
        );
    }

    public Map<Muscle.Category,List<MusclesDTO.Muscle>> getGroupedMuscles() {
        log.debug("getGroupedMuscles");
        return exerciseMapper.getGroupedMuscles();
    }

    @CacheEvict(value = "exercise", allEntries = true)
    public void deleteExercise(Long id) {
        log.debug("deleteExercise with id: {}", id);
        exerciseRepository.deleteById(id);
    }

    
    public PageDTO<ExercisesDTO.Exercise> getExerciseByFilter(String muscleName, Pageable pageable, UUID uuid){
        Muscle muscle = null;
        if (muscleName != null) {
            muscle = Muscle.valueOf(muscleName);
        }
        Page<Exercise> page = exerciseRepository.findByFilter(muscle, uuid,pageable);
        return new PageDTO<ExercisesDTO.Exercise>(
            page.getContent().stream().map(exerciseMapper::toDTO).toList(), 
            page.getPageable().getPageNumber(), 
            page.getTotalPages()
        );
    }
}
