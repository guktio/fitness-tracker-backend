package com.fitness.application.gym.plan.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.plan.entity.WorkoutPlan;

@Repository
public interface PlanRepository extends JpaRepository<WorkoutPlan, Long>{
    
    
    Page<WorkoutPlan> getPlansByCreatedByUuid(Pageable pageable, UUID userUuid);
}
