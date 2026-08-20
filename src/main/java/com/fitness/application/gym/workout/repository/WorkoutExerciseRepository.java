package com.fitness.application.gym.workout.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.workout.entity.WorkoutExercise;

/**
 * WorkoutExerciseRepository
 */
@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise,Long>{
   List<WorkoutExercise> findByWorkoutIdOrderByOrderNumAsc(Long workoutId);

   Optional<WorkoutExercise> findByWorkoutIdAndExerciseId(Long wId, Long exId);
}
