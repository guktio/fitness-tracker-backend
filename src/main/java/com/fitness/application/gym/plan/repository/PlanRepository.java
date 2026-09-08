package com.fitness.application.gym.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.plan.entity.WorkoutPlan;

@Repository
public interface PlanRepository extends JpaRepository<WorkoutPlan, Long>{
    
}
