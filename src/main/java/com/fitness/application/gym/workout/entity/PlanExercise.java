package com.fitness.application.gym.workout.entity;

import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.users.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
public class PlanExercise {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workoutPlan_id", nullable = false)
    private WorkoutPlan workoutPlan;
    
    @ManyToOne(optional = false)
    private User createdBy;
}
