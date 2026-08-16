package com.fitness.application.gym.workout;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
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
import com.fitness.application.security.UserDetailsImpl;
import com.fitness.application.users.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/gym")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/workout/start")
    public Workout startWorkout(Authentication authentication) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        logger.info("POST /workout/start with Authentication: {}", authentication.toString());
        return workoutService.startWorkout(user);
    }

    @PostMapping("/workout/stop/{id}")
    public Workout stopWorkout(@PathVariable Long id) {
        logger.info("POST /workout/stop/{}", id);
        return workoutService.completeWorkout(id);
    }

    @GetMapping("/workout/{id}")
    public WorkoutInfo getWorkoutInfo(@PathVariable Long id) {
        logger.info("GET /workout/{}", id);
        return workoutService.getWorkoutInfo(id);
    }

    @PostMapping("/workout/addExerciseToWorkout/{id}")
    public WorkoutExerciseDTO addExerciseToWorkoutDTO(
        @PathVariable Long id,
        @RequestBody ExerciseAddDTO exerciseAddDTO,
        Authentication authentication
    ) {
        logger.info("POST /workout/addExerciseToWorkout/{} with DTO: {} & Authentication: {}", id, exerciseAddDTO.toString(), authentication.toString());
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        return workoutService.addExerciseToWorkout(id, exerciseAddDTO, user);
    }

    @PostMapping("/workout-exercise/{workoutExerciseId}/set")
    public WorkoutSetDTO addSet(
            @PathVariable Long workoutExerciseId,
            @RequestBody WorkoutSet set,
            Authentication authentication) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        logger.info("POST /workout-exercise/{}/set with DTO: {} & Authentication: {}" , workoutExerciseId, set.toString(), authentication.toString());
        return workoutService.addSetToWorkoutExercise(workoutExerciseId, set, user);
    }

    @GetMapping("/user-workout/{uuid}")
    public SliceDTO<SimpleWorkoutDTO> getWorkoutByUser(
        @PathVariable UUID uuid,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        logger.info("GET /user-workout/{} with {}", uuid , pageable.toString());
        return workoutService.getWorkoutSlice(uuid, pageable);
    }

    
}