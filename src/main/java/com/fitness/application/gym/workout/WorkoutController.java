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

import com.fitness.application.base.dto.PageDTO;
import com.fitness.application.base.dto.SliceDTO;
import com.fitness.application.gym.set.SetsDTO;
import com.fitness.application.security.CurrentUser;
import com.fitness.application.users.UserService;
import com.fitness.application.users.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/gym")
@RequiredArgsConstructor
@Slf4j
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;

    @GetMapping("/workouts/my")
    public ResponseEntity<PageDTO<WorkoutsDTO.Workout>> getWorkoutPage(
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
        @CurrentUser User user
    ){
        log.info("GET /workouts");
        return ResponseEntity.status(HttpStatus.OK).body(workoutService.getAllWorkouts(user,pageable));
    }

    @PostMapping("/workout/start")
    public ResponseEntity<WorkoutsDTO.Workout> startWorkout(
        @CurrentUser User user
    ) {
        userService.getUserOrThrowNotFound(user);
        log.info("POST /workout/start with Authentication: {}", user.toString());
        WorkoutsDTO.Workout workout = workoutService.startWorkout(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(workout);
    }

    @PostMapping("/workout/{id}/complete")
    public ResponseEntity<WorkoutsDTO.Workout> stopWorkout(
        @PathVariable Long id, 
        @CurrentUser User user
    ) {
        log.info("POST /workout/{}/complete", id);
        WorkoutsDTO.Workout workout = workoutService.completeWorkout(id, user);
        return ResponseEntity.ok(workout);
    }

    @GetMapping("/workout/{id}")
    public ResponseEntity<WorkoutsDTO.Info> getWorkoutInfo(
        @PathVariable Long id
    ) {
        log.info("GET /workout/{}", id);
        WorkoutsDTO.Info body = workoutService.getWorkoutInfo(id);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/workout/{workoutId}/exercise/{exerciseId}")
    public ResponseEntity<WorkoutsDTO.WorkoutExercise> addExerciseToWorkoutDTO(
        @PathVariable Long workoutId,
        @PathVariable Long exerciseId,
        @RequestBody WorkoutsDTO.AddExercise exerciseAddDTO,
        @CurrentUser User user
    ) {
        userService.getUserOrThrowNotFound(user);
        log.info("POST /workout/exercise/{} with DTO: {} & Authentication: {}", workoutId, exerciseAddDTO.toString(), user.toString());
        WorkoutsDTO.WorkoutExercise body = workoutService.addExerciseToWorkout(workoutId, exerciseId,exerciseAddDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/workout/exercise/{workoutExerciseId}/set")
    public ResponseEntity<SetsDTO.Set> addSet(
            @PathVariable Long workoutExerciseId,
            @RequestBody SetsDTO.Create set,
            @CurrentUser User user
    ) {
        userService.getUserOrThrowNotFound(user);
        log.info("POST /workout/exercise/{}/set with DTO: {} & Authentication: {}" , workoutExerciseId, set.toString(), user.toString());
        var response = workoutService.addSetToWorkoutExercise(workoutExerciseId, set, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/workout/exercise/{workoutExerciseId}/set/{setId}")
    public ResponseEntity<Void> removeSet(
            @PathVariable Long workoutExerciseId,
            @PathVariable Long setId,
            @CurrentUser User user
    ) {
        userService.getUserOrThrowNotFound(user);
        log.info("DELETE /workout/exercise/{}/set/{} & Authentication: {}" , workoutExerciseId, setId, user.toString());
        workoutService.deleteSetFromExercise(workoutExerciseId, setId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/workout/{uuid}")
    public ResponseEntity<SliceDTO<WorkoutsDTO.Workout>> getWorkoutByUser(
        @PathVariable UUID uuid,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("GET /user/workout/{} with {}", uuid , pageable.toString());
        SliceDTO<WorkoutsDTO.Workout> slice = workoutService.getWorkoutSlice(uuid, pageable);
        return ResponseEntity.ok(slice);
    }

    @DeleteMapping("/workout/{wId}/exercise/{exId}")
    public ResponseEntity<Void> deleteExerciseFromWorkout(
        @PathVariable Long wId, 
        @PathVariable Long exId, 
        @CurrentUser User user
    ) {
        workoutService.deleteExerciseFromWorkout(wId, exId,user);
        log.info("DELETE /workout/{}/exercise/{}", wId, exId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/workout/{wid}/plan/{pid}")
    public ResponseEntity<WorkoutsDTO.Info> useWorkoutPlan(
        @PathVariable Long wid,
        @PathVariable Long pid,
        @CurrentUser User user
    ){
        log.info("POST /workout/{}/plan/{}", wid, pid);
        return ResponseEntity.status(HttpStatus.OK).body(workoutService.createWorkoutByPlan(pid, wid,user));
    }
}
