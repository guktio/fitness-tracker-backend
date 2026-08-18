package com.fitness.application.gym.exercises.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.exercises.entity.Muscle;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    boolean existsByNameIgnoreCase(String name);

    void deleteById(Long id);

    Page<Exercise> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
        "createdBy",
        "exerciseMuscles",
        "createdBy.roles",
        "exerciseMuscles.primaryMuscles", 
        "exerciseMuscles.secondaryMuscles", 
        "exerciseMuscles.stabilizingMuscles"
    })
    Optional<Exercise> findById(Long id);

    @Query("""
        SELECT e FROM Exercise e
        JOIN e.exerciseMuscles.primaryMuscles as pm
        WHERE (:muscle IS NULL OR pm.muscle  = :muscle)
        AND (:createdBy IS NULL or e.createdBy = :createdBy)
    """)
    Page<Exercise> findByFilter(Muscle muscle, UUID createdBy, Pageable pageable);
}