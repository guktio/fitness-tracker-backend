package com.fitness.application.gym.plan;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.application.base.dto.PageDTO;
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
    public ResponseEntity<PlansDTO.Plan> getWorkoutPlan(
        @PathVariable Long id
    ){
        log.info("GET /workout/plan/{} ", id);
        return ResponseEntity.status(HttpStatus.OK).body(planService.getWorkoutPlanDTO(id));
    }

    @PostMapping("/workout/plan")
    public ResponseEntity<PlansDTO.Plan> createWorkoutPlan(
        @RequestBody PlansDTO.CreatePlan workoutPlan,
        @CurrentUser User user
    ){
        log.info("POST /workout/plan {}", workoutPlan.toString());
        return ResponseEntity.status(HttpStatus.OK).body(planService.createWorkoutPlan(workoutPlan, user));
    }

    @PostMapping("/workout/plan/{pid}/exercise/{eid}")
    public ResponseEntity<PlansDTO.Plan> addExerciseToPlan(
        @PathVariable Long pid,
        @PathVariable Long eid,
        @RequestParam int orderNum,
        @CurrentUser User user
    ){
        log.info("POST /workout/plan/{}/exercise/{}?orderNum={}", pid, eid, orderNum);
        return ResponseEntity.status(HttpStatus.OK).body(planService.addExerciseToPlan(pid, eid, orderNum, user));
    }

    @GetMapping("/workout/plans")
    public ResponseEntity<PageDTO<PlansDTO.Plan>> getPlansPage(
        @RequestParam UUID user,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("GET /workout/plans {}", user);
        return ResponseEntity.status(HttpStatus.OK).body(planService.getPlansPage(pageable,user));
    }
}