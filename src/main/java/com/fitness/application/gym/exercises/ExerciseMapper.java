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

import com.fitness.application.gym.exercises.dto.ExercisesDTO;
import com.fitness.application.gym.exercises.dto.MusclesDTO;
import com.fitness.application.gym.exercises.entity.Exercise;
import com.fitness.application.gym.exercises.entity.ExerciseMuscle;
import com.fitness.application.gym.exercises.entity.ExerciseMuscle.MuscleImpact;
import com.fitness.application.gym.exercises.entity.Muscle;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExerciseMapper {

    private final MessageSource messageSource;

    public ExercisesDTO.Exercise toDTO(Exercise exercise) {
        return ExercisesDTO.Exercise.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .description(exercise.getDescription())
                .username(exercise.getCreatedBy().getUsername())
                .userUuid(exercise.getCreatedBy().getUuid())
                .muscleGroup(ExercisesDTO.ExerciseMuscle.builder()
                        .primaryMuscles(mapMuscleImpact(exercise.getExerciseMuscles().getPrimaryMuscles()))
                        .secondaryMuscles(mapMuscleImpact(exercise.getExerciseMuscles().getSecondaryMuscles()))
                        .stabilizingMuscles(mapMuscleImpact(exercise.getExerciseMuscles().getStabilizingMuscles()))
                        .build())
                .build();
    }

    private Set<ExercisesDTO.MuscleImpact> mapMuscleImpact(Collection<MuscleImpact> impacts) {
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

                    return ExercisesDTO.MuscleImpact.builder()
                            .code(muscle.name())
                            .name(localizedName)
                            .impactLevel(impact.getImpactPercentage())
                            .build();
                })
                .collect(Collectors.toSet());
    }

    public Exercise toEntity(ExercisesDTO.CreateExercise dto) {
        return Exercise.builder()
                            .name(dto.name())
                            .description(dto.description())
                            .exerciseMuscles(toExerciseMuscleEntity(dto.muscles()))
                            .build();
    }

    public ExerciseMuscle toExerciseMuscleEntity(ExercisesDTO.ExerciseMuscle dto) {
        if (dto == null) {
            return new ExerciseMuscle();
        }
        return ExerciseMuscle.builder()
                .primaryMuscles(toMuscleImpactSet(dto.primaryMuscles()))
                .secondaryMuscles(toMuscleImpactSet(dto.secondaryMuscles()))
                .stabilizingMuscles(toMuscleImpactSet(dto.stabilizingMuscles()))
                .build();
    }

    public Set<MuscleImpact> toMuscleImpactSet(Set<ExercisesDTO.MuscleImpact> dtos) {
        if (dtos == null) {
            return new HashSet<>();
        }

        return dtos.stream()
                .filter(dto -> dto != null && dto.code() != null)
                .map(dto -> {
                    return MuscleImpact.builder()
                            .muscle(Muscle.valueOf(dto.code()))
                            .impactPercentage(dto.impactLevel())
                            .build();
                })
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Map<Muscle.Category,List<MusclesDTO.Muscle>> getGroupedMuscles() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return Arrays.stream(Muscle.values())
                        .collect(Collectors.groupingBy(
                            m -> m.getCategory(),
                            Collectors.mapping(m -> (
                                MusclesDTO.Muscle.builder().code(m.name()).name(messageSource.getMessage(m.getCode(), null,m.name(), currentLocale)).build())
                                , Collectors.toList())));   
    }
}