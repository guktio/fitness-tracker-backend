package com.fitness.application.gym.exercises;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.application.gym.exercises.DTO.CreateExerciseDTO;
import com.fitness.application.gym.exercises.DTO.ExerciseDTO;
import com.fitness.application.gym.exercises.DTO.PageDTO;
import com.fitness.application.gym.exercises.entity.Muscle;
import com.fitness.application.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gym")
public class ExerciseController {

    private final ExerciseService exerciseService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/exercise")
    public ExerciseDTO createExercise(@RequestBody CreateExerciseDTO exercise, Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        logger.info("POST /api/gym/exercise - Creating exercise with name: {} for user: {}", exercise.getName(), principal.getUser().getUuid());
        return exerciseService.createExercise(exercise, principal.getUser());
    }

    @GetMapping("/exercise/{id}")
    public ExerciseDTO getExerciseById(@PathVariable Long id, Locale locale) {
        logger.info("GET /api/gym/exercise/{} - Fetch exercise bt id: {} witj locale: {} ", id, id, locale);
        return exerciseService.getExerciseById(id, locale);
    }

    @GetMapping("/exercise")
    public PageDTO<ExerciseDTO> getAllExercises(Pageable pageable) {
        logger.info("GET /api/gym/exercise - Fetching all exercises");
        return exerciseService.getAllExercises(pageable);
    }

    @DeleteMapping("/exercise/{id}")
    public void deleteExercise(@PathVariable Long id) {
        logger.info("DELETE /api/gym/exercise/{} - Deleting exercise", id);
        exerciseService.deleteExercise(id);
    }

    @GetMapping("/exercise/muscle")
    public Map<Muscle.Category,List<Muscle>> getMuscles(Locale locale){
        return exerciseService.getGroupedMuscles(locale);
    }
}
