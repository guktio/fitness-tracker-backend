package com.fitness.application.gym.exercises;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.fitness.application.gym.exercises.DTO.CreateExerciseDTO;
import com.fitness.application.gym.exercises.DTO.ExerciseDTO;
import com.fitness.application.gym.exercises.DTO.ExerciseMuscleDTO;
import com.fitness.application.gym.exercises.DTO.MuscleImpactDTO;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.exercises.entity.ExerciseMuscle;
import com.fitness.application.gym.exercises.entity.ExerciseMuscle.MuscleImpact;
import com.fitness.application.gym.exercises.entity.Muscle;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExerciseMapper {

    private final MessageSource messageSource;

    public ExerciseDTO toDTO(Exercise exercise) {
        return ExerciseDTO.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .description(exercise.getDescription())
                .username(exercise.getCreatedBy().getUsername())
                .userUuid(exercise.getCreatedBy().getUuid())
                .muscleGroup(ExerciseMuscleDTO.builder()
                        .primaryMuscles(mapMuscleImpact(exercise.getExerciseMuscles().getPrimaryMuscles()))
                        .secondaryMuscles(mapMuscleImpact(exercise.getExerciseMuscles().getSecondaryMuscles()))
                        .stabilizingMuscles(mapMuscleImpact(exercise.getExerciseMuscles().getStabilizingMuscles()))
                        .build())
                .build();
    }

    private Set<MuscleImpactDTO> mapMuscleImpact(Collection<MuscleImpact> impacts) {
        if (impacts == null) {
            return Collections.emptySet();
        }

        Locale currentLocale = LocaleContextHolder.getLocale();

        return impacts.stream()
                .map(impact -> {
                    Muscle muscle = impact.getMuscle();

                    String localizedName = messageSource.getMessage(
                            muscle.getCode(),
                            null,
                            muscle.name(),
                            currentLocale
                    );

                    return MuscleImpactDTO.builder()
                            .muscle(muscle.name())
                            .name(localizedName)
                            .impactLevel(impact.getImpactPercentage())
                            .build();
                })
                .collect(Collectors.toSet());
    }

    public Exercise toEntity(CreateExerciseDTO dto) {
        return Exercise.builder()
                            .name(dto.getName())
                            .description(dto.getDescription())
                            .exerciseMuscles(toExerciseMuscleEntity(dto.getMuscles()))
                            .build();
    }

    public ExerciseMuscle toExerciseMuscleEntity(ExerciseMuscleDTO exerciseMuscleDTO) {
        if (exerciseMuscleDTO == null) {
            return new ExerciseMuscle();
        }
        return ExerciseMuscle.builder()
                .primaryMuscles(toMuscleImpactSet(exerciseMuscleDTO.getPrimaryMuscles()))
                .secondaryMuscles(toMuscleImpactSet(exerciseMuscleDTO.getSecondaryMuscles()))
                .stabilizingMuscles(toMuscleImpactSet(exerciseMuscleDTO.getStabilizingMuscles()))
                .build();
    }

    public Set<MuscleImpact> toMuscleImpactSet(Set<MuscleImpactDTO> dtos) {
        if (dtos == null) {
            return new HashSet<>();
        }

        return dtos.stream()
                .filter(dto -> dto != null && dto.getMuscle() != null)
                .map(dto -> {
                    return MuscleImpact.builder()
                            .muscle(Muscle.valueOf(dto.getMuscle()))
                            .impactPercentage(dto.getImpactLevel())
                            .build();
                })
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Map<Muscle.Category,List<Muscle>> getGroupedMuscles(Locale locale) {
        return Arrays.stream(Muscle.values()).collect(Collectors.groupingBy(m -> m.getCategory()));   
    }
}