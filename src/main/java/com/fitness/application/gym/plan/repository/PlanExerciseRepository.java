package com.fitness.application.gym.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.plan.entity.PlanExercise;

@Repository 
public interface PlanExerciseRepository extends JpaRepository<PlanExercise, Long>{
    
}
