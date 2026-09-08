package com.fitness.application.gym.workout.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.workout.entity.Workout;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Optional<Workout> findWorkoutById(Long id);
    
    Page<Workout> findAllByCreatedByUuid(UUID uuid, Pageable pageable);

    Page<Workout> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"exercises", "exercises.exercise"})
    Optional<Workout> findWithDetailsById(Long id);
}
