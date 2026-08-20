package com.fitness.application.gym.workout.entity;

import java.util.ArrayList;
import java.util.List;

import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.users.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercise {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    private Integer orderNum;

    @ManyToOne(optional = false)
    private User createdBy;

    @Builder.Default
    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutSet> sets = new ArrayList<>();

    public void addSet(WorkoutSet set){
        if (set != null) {
            sets.add(set);
            set.setWorkoutExercise(this);
        }
    }

    public void removeSet(WorkoutSet set) {
        if (set != null) {
            sets.remove(set);
            set.setWorkoutExercise(null);
        }  
    }
}