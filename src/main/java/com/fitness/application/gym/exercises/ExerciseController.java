package com.fitness.application.gym.exercises;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody CreateExerciseDTO exercise, @CurrentUser User user) {
        userService.getUserOrThrowNotFound(user);
        log.info("POST /api/gym/exercise - Creating exercise with name: {} for user: {}", exercise.getName(), user.getUuid());
        ExerciseDTO response = exerciseService.createExercise(exercise, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/exercise/{id}")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable Long id) {
        log.info("GET /api/gym/exercise/{} - Fetch exercise by id: {}", id, id);
        ExerciseDTO response = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exercise")
    public ResponseEntity<PageDTO<ExerciseDTO>> getAllExercises(
        Pageable pageable,
        @RequestParam(required = false) String muscle,
        @RequestParam(required = false) UUID createdBy
    ) {
        log.info("GET /api/gym/exercise - Fetching all exercises");
        PageDTO<ExerciseDTO> response = exerciseService.getExerciseByFilter(muscle, pageable, createdBy);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        log.info("DELETE /api/gym/exercise/{} - Deleting exercise", id);
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exercise/muscle")
    public ResponseEntity<Map<Muscle.Category, List<MuscleDTO>>> getMuscles() {
        Map<Muscle.Category, List<MuscleDTO>> response = exerciseService.getGroupedMuscles();
        return ResponseEntity.ok(response);
    }
}