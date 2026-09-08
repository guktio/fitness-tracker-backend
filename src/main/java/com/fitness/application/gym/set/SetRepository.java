package com.fitness.application.gym.set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * WorkoutSetRepository
 */
@Repository
public interface SetRepository extends JpaRepository<WorkoutSet, Long>{
}
