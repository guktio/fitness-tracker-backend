package com.fitness.application.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.application.users.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,UUID>{

    Optional<User> findByEmail(String username);

    Page<User> findAll(Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
