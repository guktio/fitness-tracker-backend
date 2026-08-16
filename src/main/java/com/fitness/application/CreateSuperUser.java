package com.fitness.application;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.application.gym.exercises.ExerciseInitializerService;
import com.fitness.application.users.UserRepository;
import com.fitness.application.users.entity.Roles;
import com.fitness.application.users.entity.User;

@Component
public class CreateSuperUser implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExerciseInitializerService initializerService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CreateSuperUser(UserRepository userRepository, 
                           PasswordEncoder passwordEncoder, 
                           ExerciseInitializerService initializerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.initializerService = initializerService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        User systemAdmin = userRepository.findByEmail("admin@example.com")
                .orElseGet(() -> {
                    User superUser = User.builder()
                            .username("admin")
                            .email("admin@example.com")
                            .password(passwordEncoder.encode("admin"))
                            .roles(Set.of(Roles.ADMIN))
                            .build();
                    User saved = userRepository.save(superUser);
                    logger.info("Super user created: {}", saved.getEmail());
                    return saved;
                });

        initializerService.seedDefaultExercises(systemAdmin);
    }
}