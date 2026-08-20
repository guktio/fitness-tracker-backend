package com.fitness.application.gym.workout.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.workout.entity.Workout;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Optional<Workout> findWorkoutById(Long id);
    
    Slice<Workout> findAllByCreatedByUuid(UUID uuid, Pageable pageable);

    @EntityGraph(attributePaths = {"exercises", "exercises.exercise"})
    Optional<Workout> findWithDetailsById(Long id);
}
