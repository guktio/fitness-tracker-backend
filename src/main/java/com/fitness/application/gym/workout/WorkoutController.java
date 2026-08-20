package com.fitness.application.gym.workout;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.application.gym.workout.DTO.ExerciseAddDTO;
import com.fitness.application.gym.workout.DTO.SimpleWorkoutDTO;
import com.fitness.application.gym.workout.DTO.SliceDTO;
import com.fitness.application.gym.workout.DTO.WorkoutExerciseDTO;
import com.fitness.application.gym.workout.DTO.WorkoutInfo;
import com.fitness.application.gym.workout.DTO.WorkoutSetDTO;
import com.fitness.application.gym.workout.entity.Workout;
import com.fitness.application.gym.workout.entity.WorkoutSet;
import com.fitness.application.security.CurrentUser;
import com.fitness.application.users.UserService;
import com.fitness.application.users.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/gym")
@RequiredArgsConstructor
@Slf4j
public class WorkoutController {

    private final WorkoutService workoutService;

    private final UserService userService;

    @PostMapping("/workout/start")
    public Workout startWorkout(@CurrentUser User user) {
        userService.getUserOrThrowNotFound(user);
        log.info("POST /workout/start with Authentication: {}", user.toString());
        return workoutService.startWorkout(user);
    }

    @PostMapping("/workout/stop/{id}")
    public Workout stopWorkout(@PathVariable Long id) {
        log.info("POST /workout/stop/{}", id);
        return workoutService.completeWorkout(id);
    }

    @GetMapping("/workout/{id}")
    public WorkoutInfo getWorkoutInfo(@PathVariable Long id) {
        log.info("GET /workout/{}", id);
        return workoutService.getWorkoutInfo(id);
    }

    @PostMapping("/workout/exercise/{id}")
    public WorkoutExerciseDTO addExerciseToWorkoutDTO(
        @PathVariable Long id,
        @RequestBody ExerciseAddDTO exerciseAddDTO,
        @CurrentUser User user
    ) {
        userService.getUserOrThrowNotFound(user);
        log.info("POST /workout/addExerciseToWorkout/{} with DTO: {} & Authentication: {}", id, exerciseAddDTO.toString(), user.toString());
        return workoutService.addExerciseToWorkout(id, exerciseAddDTO, user);
    }

    @PostMapping("/workout/exercise/{workoutExerciseId}/set")
    public WorkoutSetDTO addSet(
            @PathVariable Long workoutExerciseId,
            @RequestBody WorkoutSet set,
            @CurrentUser User user
    ) {
        userService.getUserOrThrowNotFound(user);
        log.info("POST /workout-exercise/{}/set with DTO: {} & Authentication: {}" , workoutExerciseId, set.toString(), user.toString());
        return workoutService.addSetToWorkoutExercise(workoutExerciseId, set, user);
    }

    @GetMapping("/user/workout/{uuid}")
    public SliceDTO<SimpleWorkoutDTO> getWorkoutByUser(
        @PathVariable UUID uuid,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("GET /user-workout/{} with {}", uuid , pageable.toString());
        return workoutService.getWorkoutSlice(uuid, pageable);
    }

    @DeleteMapping("/workout/{wId}/exercise/{exId}")
    public ResponseEntity<HttpStatus> deleteExerciseFromWorkout(@PathVariable Long wId, @PathVariable Long exId){
        workoutService.deleteExerciseFromWorkout(wId, exId);
        return ResponseEntity.ok().build();
    }
}