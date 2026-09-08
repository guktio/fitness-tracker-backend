package com.fitness.application.gym.plan;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.application.gym.plan.dto.WorkoutPlanDTO;
import com.fitness.application.gym.plan.entity.WorkoutPlan;
import com.fitness.application.security.CurrentUser;
import com.fitness.application.users.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/gym")
@RequiredArgsConstructor
@Slf4j
public class PlanController {

    private final PlanService planService;

    @GetMapping("/workout/plan/{id}")
    public ResponseEntity<WorkoutPlanDTO> getWorkoutPlan(
        @PathVariable Long id
    ){
        log.info("GET /workout/plan/{} ", id);
        return ResponseEntity.status(HttpStatus.OK).body(planService.getWorkoutPlanDTO(id));
    }

    @PostMapping("/workout/plan")
    public ResponseEntity<WorkoutPlanDTO> createWorkoutPlan(
        @RequestBody WorkoutPlan workoutPlan
    ){
        log.info("POST /workout/plan {}", workoutPlan.toString());
        return ResponseEntity.status(HttpStatus.OK).body(planService.createWorkoutPlan(workoutPlan));
    }

    @PostMapping("/workout/plan/{pid}/exercise/{eid}")
    public ResponseEntity<WorkoutPlanDTO> addExerciseToPlan(
        @PathVariable Long pid,
        @PathVariable Long eid,
        @RequestParam int orderNum,
        @CurrentUser User user
    ){
        log.info("POST /workout/plan/{}/exercise/{}?orderNum={}", pid, eid, orderNum);
        return ResponseEntity.status(HttpStatus.OK).body(planService.addExerciseToPlan(pid, eid, orderNum, user));
    }
}
