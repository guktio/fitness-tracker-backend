package com.fitness.application.gym.workout.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.workout.entity.Workout;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Optional<Workout> findWorkoutById(Long id);
    
    @Query("SELECT w FROM Workout w WHERE w.createdBy.uuid = :userUuid")
    Slice<Workout> findAllByCreatedBy(@Param("userUuid") UUID userUuid, Pageable pageable);

    @EntityGraph(attributePaths = {"exercises", "exercises.exercise"})
    Optional<Workout> findWithDetailsById(Long id);
}
