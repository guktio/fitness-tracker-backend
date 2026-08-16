package com.fitness.application.gym.workout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.workout.entity.WorkoutSet;

/**
 * WorkoutSetRepository
 */
@Repository
public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long>{
    List<WorkoutSet> findByWorkoutExerciseIdInOrderBySetNumberAsc(List<Long> workoutExerciseIds);
}
