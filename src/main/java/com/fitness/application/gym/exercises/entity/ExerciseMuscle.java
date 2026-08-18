package com.fitness.application.gym.exercises.entity;

import java.util.Set;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMuscle {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exercise_primary_muscles", joinColumns = @JoinColumn(name = "exercise_id"))
    private Set<MuscleImpact> primaryMuscles;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exercise_secondary_muscles", joinColumns = @JoinColumn(name = "exercise_id"))
    private Set<MuscleImpact> secondaryMuscles;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exercise_stabilizing_muscles", joinColumns = @JoinColumn(name = "exercise_id"))
    private Set<MuscleImpact> stabilizingMuscles;

    @Embeddable 
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class MuscleImpact {
        @Enumerated(EnumType.STRING)
        private Muscle muscle;
        private Integer impactPercentage;
    }
}