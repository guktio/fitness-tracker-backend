package com.fitness.application.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fitness.application.base.dto.PageDTO;
import com.fitness.application.exceptions.AccessDeniedException;
import com.fitness.application.users.dto.UserRequestDTO;
import com.fitness.application.users.dto.UserResponseDTO;
import com.fitness.application.users.entity.Roles;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User regularUser;
    private User adminUser;
    private User targetUser;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        regularUser = User.builder()
                .uuid(userId)
                .username("regular")
                .email("regular@example.com")
                .roles(Set.of(Roles.USER))
                .build();

        adminUser = User.builder()
                .uuid(UUID.randomUUID())
                .username("admin")
                .email("admin@example.com")
                .roles(Set.of(Roles.ADMIN))
                .build();

        targetUser = User.builder()
                .uuid(UUID.randomUUID())
                .username("target")
                .email("target@example.com")
                .roles(Set.of(Roles.USER))
                .build();

        userRequestDTO = UserRequestDTO.builder()
                .username("updatedName")
                .email("updated@example.com")
                .build();

        userResponseDTO = UserResponseDTO.builder()
                .uuid(userId)
                .username("updatedName")
                .email("updated@example.com")
                .roles(Set.of(Roles.USER))
                .build();
    }

    @Test
    void create_shouldSaveAndReturnDto() {
        when(userMapper.toEntity(userRequestDTO)).thenReturn(regularUser);
        when(userRepository.save(regularUser)).thenReturn(regularUser);
        when(userMapper.toResponseDto(regularUser)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.create(userRequestDTO);

        assertNotNull(result);
        assertEquals("updatedName", result.getUsername());
        verify(userRepository).save(regularUser);
    }

    @Test
    void checkUsername_shouldReturnTrueWhenAvailable_andFalseWhenTaken() {
        when(userRepository.existsByUsername("available")).thenReturn(false);
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertTrue(userService.checkUsername("available"));
        assertFalse(userService.checkUsername("taken"));
    }

    @Test
    void checkEmail_shouldReturnTrueWhenAvailable_andFalseWhenTaken() {
        when(userRepository.existsByEmail("free@example.com")).thenReturn(false);
        when(userRepository.existsByEmail("used@example.com")).thenReturn(true);

        assertTrue(userService.checkEmail("free@example.com"));
        assertFalse(userService.checkEmail("used@example.com"));
    }

    @Test
    void update_whenSelfUpdating_shouldSucceed() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(regularUser));
        when(userRepository.save(regularUser)).thenReturn(regularUser);
        when(userMapper.toResponseDto(regularUser)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.update(regularUser, userId, userRequestDTO);

        assertNotNull(result);
        assertEquals("updatedName", regularUser.getUsername());
        assertEquals("updated@example.com", regularUser.getEmail());
    }

    @Test
    void update_whenAdminUpdatingOtherUser_shouldSucceed() {
        UUID targetId = targetUser.getUuid();
        when(userRepository.findById(targetId)).thenReturn(Optional.of(targetUser));
        when(userRepository.save(targetUser)).thenReturn(targetUser);
        when(userMapper.toResponseDto(targetUser)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.update(adminUser, targetId, userRequestDTO);

        assertNotNull(result);
        assertEquals("updatedName", targetUser.getUsername());
    }

    @Test
    void update_whenNonAdminUpdatingOtherUser_shouldThrowAccessDeniedException() {
        UUID targetId = targetUser.getUuid();
        when(userRepository.findById(targetId)).thenReturn(Optional.of(targetUser));

        assertThrows(AccessDeniedException.class, () -> userService.update(regularUser, targetId, userRequestDTO));
    }

    @Test
    void update_whenUserNotFound_shouldThrowEntityNotFoundException() {
        UUID unknownId = UUID.randomUUID();
        when(userRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.update(regularUser, unknownId, userRequestDTO));
    }

    @Test
    void delete_whenSelfDeleting_shouldSucceed() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(regularUser));

        userService.delete(regularUser, userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void delete_whenAdminDeletingOtherUser_shouldSucceed() {
        UUID targetId = targetUser.getUuid();
        when(userRepository.findById(targetId)).thenReturn(Optional.of(targetUser));

        userService.delete(adminUser, targetId);

        verify(userRepository).deleteById(targetId);
    }

    @Test
    void delete_whenNonAdminDeletingOtherUser_shouldThrowAccessDeniedException() {
        UUID targetId = targetUser.getUuid();
        when(userRepository.findById(targetId)).thenReturn(Optional.of(targetUser));

        assertThrows(AccessDeniedException.class, () -> userService.delete(regularUser, targetId));
    }

    @Test
    void getAll_shouldReturnPageOfUsers() {
        Page<User> page = new PageImpl<>(List.of(regularUser), Pageable.ofSize(10).withPage(0), 1);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(userMapper.toResponseDto(regularUser)).thenReturn(userResponseDTO);

        PageDTO<UserResponseDTO> result = userService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(0, result.page());
        assertEquals(1, result.totalPages());
    }

    @Test
    void getByUuid_whenFound_shouldReturnDto() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(regularUser));
        when(userMapper.toResponseDto(regularUser)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getByUuid(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUuid());
    }

    @Test
    void getByUuid_whenNotFound_shouldThrowEntityNotFoundException() {
        UUID unknownId = UUID.randomUUID();
        when(userRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getByUuid(unknownId));
    }

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnUserDetails() {
        when(userRepository.findByEmail("regular@example.com")).thenReturn(Optional.of(regularUser));

        UserDetails userDetails = userService.loadUserByUsername("regular@example.com");

        assertNotNull(userDetails);
        assertEquals("regular@example.com", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_whenUserDoesNotExist_shouldThrowUsernameNotFoundException() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("missing@example.com"));
    }

    @Test
    void getUserOrThrowNotFound_withNull_shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> userService.getUserOrThrowNotFound(null));
    }

    @Test
    void getUserOrThrowNotFound_withValidUser_shouldReturnUser() {
        User result = userService.getUserOrThrowNotFound(regularUser);
        assertEquals(regularUser, result);
    }
}

