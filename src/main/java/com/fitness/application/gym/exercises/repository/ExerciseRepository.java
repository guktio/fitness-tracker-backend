package com.fitness.application.gym.exercises.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.gym.exercises.entity.Exercise;

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
}