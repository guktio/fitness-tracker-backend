package com.fitness.application.gym.plan.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@Entity
@ToString(exclude = {"exercises"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String title;

    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PlanExercise> exercises = new ArrayList<>();

    public void addExercise(PlanExercise exercise) {
        if (exercise != null) {
            exercises.add(exercise);
            exercise.setWorkoutPlan(this);
        }
    }

    public void removeExercise(PlanExercise exercise) {
        if (exercise != null) {
            exercises.remove(exercise);
            exercise.setWorkoutPlan(null);
        }
    }
}