package com.fitness.application.gym.workout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.workout.entity.PlanExercise;

@Repository 
public interface WorkoutPlanExerciseRepository extends JpaRepository<PlanExercise, Long>{
    
}
