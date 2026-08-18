package com.fitness.application.gym.exercises;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.application.base.DTO.PageDTO;
import com.fitness.application.gym.exercises.DTO.CreateExerciseDTO;
import com.fitness.application.gym.exercises.DTO.ExerciseDTO;
import com.fitness.application.gym.exercises.DTO.muscles.MuscleDTO;
import com.fitness.application.gym.exercises.entity.Muscle;
import com.fitness.application.security.CurrentUser;
import com.fitness.application.users.UserService;
import com.fitness.application.users.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gym")
@Slf4j
public class ExerciseController {

    private final ExerciseService exerciseService;

    private final UserService userService;

    @PostMapping("/exercise")
    public ExerciseDTO createExercise(@RequestBody CreateExerciseDTO exercise, @CurrentUser User user) {
        userService.getUserOrThrowNotFound(user);
        log.info("POST /api/gym/exercise - Creating exercise with name: {} for user: {}", exercise.getName(), user.getUuid());
        return exerciseService.createExercise(exercise, user);
    }

    @GetMapping("/exercise/{id}")
    public ExerciseDTO getExerciseById(@PathVariable Long id) {
        log.info("GET /api/gym/exercise/{} - Fetch exercise bt id: {}", id, id);
        return exerciseService.getExerciseById(id);
    }

    @GetMapping("/exercise")
    public PageDTO<ExerciseDTO> getAllExercises(
        Pageable pageable,
        @RequestParam(required = false) String muscle,
        @RequestParam(required = false) UUID createdBy
    ) {
        log.info("GET /api/gym/exercise - Fetching all exercises");
        return exerciseService.getExerciseByFilter(muscle, pageable, createdBy);
    }

    @DeleteMapping("/exercise/{id}")
    public void deleteExercise(@PathVariable Long id) {
        log.info("DELETE /api/gym/exercise/{} - Deleting exercise", id);
        exerciseService.deleteExercise(id);
    }

    @GetMapping("/exercise/muscle")
    public Map<Muscle.Category,List<MuscleDTO>> getMuscles(){
        return exerciseService.getGroupedMuscles();
    }
}
