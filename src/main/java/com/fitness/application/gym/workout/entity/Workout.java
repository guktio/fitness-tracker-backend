package com.fitness.application.gym.workout.entity;

import java.util.ArrayList;
import java.util.List;

import com.fitness.application.base.BaseEntity;
import com.fitness.application.users.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Workout extends BaseEntity {

    public enum Status {
        IN_PROGRESS,
        COMPLETED
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Status status;

    @ManyToOne(optional = false)
    private User createdBy;

    @Builder.Default
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<WorkoutExercise> exercises = new ArrayList<>();

    public void addExercise(WorkoutExercise exercise) {
        if (exercise != null) {
            exercises.add(exercise);
            exercise.setWorkout(this);
        }
    }

    public void removeExercise(WorkoutExercise exercise) {
        if (exercise != null) {
            exercises.remove(exercise);
            exercise.setWorkout(null);
        }
    }
}
    