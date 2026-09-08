package com.fitness.application.gym.plan;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.application.gym.exercises.ExerciseService;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.plan.dto.WorkoutPlanDTO;
import com.fitness.application.gym.plan.entity.PlanExercise;
import com.fitness.application.gym.plan.entity.WorkoutPlan;
import com.fitness.application.gym.plan.repository.PlanExerciseRepository;
import com.fitness.application.gym.plan.repository.PlanRepository;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service
@RequiredArgsConstructor 
public class PlanService {

    private final PlanRepository planRepository;

    private final PlanExerciseRepository planExerciseRepository;

    private final ExerciseService exerciseService;

    private final PlanMapper planMapper;

    public WorkoutPlan getWorkoutPlanOrThrow(Long id){
        return planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("WorkoutPlan with id:"+id+" not found"));
    }

    public WorkoutPlanDTO getWorkoutPlanDTO(Long id){
        return planMapper.toWorkoutPlanDTO(getWorkoutPlanOrThrow(id));
    }

    public WorkoutPlanDTO createWorkoutPlan(WorkoutPlan workoutPlan){
        WorkoutPlan saved = planRepository.save(workoutPlan);
        return planMapper.toWorkoutPlanDTO(saved);
    }
    
    @Transactional
    public WorkoutPlanDTO addExerciseToPlan(long pid, long eid, int orderNum, User user){
        WorkoutPlan plan = getWorkoutPlanOrThrow(pid);
        Exercise exercise = exerciseService.getExerciseOrThrow(eid);
        PlanExercise pExercise = PlanExercise.builder()
                                        .orderNum(orderNum)
                                        .exercise(exercise)
                                        .workoutPlan(plan)
                                        .createdBy(user)
                                        .build();
        planExerciseRepository.saveAndFlush(pExercise);
        plan.getExercises().add(pExercise);
        WorkoutPlan newPlan = planRepository.save(plan);
        log.info(newPlan.toString());
        return planMapper.toWorkoutPlanDTO(newPlan);
    }

    
}
