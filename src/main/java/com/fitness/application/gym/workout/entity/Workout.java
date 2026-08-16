package com.fitness.application.gym.workout.entity;

import com.fitness.application.base.BaseEntity;
import com.fitness.application.users.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@NoArgsConstructor
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
}
    