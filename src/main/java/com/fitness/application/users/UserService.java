package com.fitness.application.users;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fitness.application.base.DTO.PageDTO;
import com.fitness.application.exceptions.AccessDeniedExecption;
import com.fitness.application.security.UserDetailsImpl;
import com.fitness.application.users.DTO.UserRequestDTO;
import com.fitness.application.users.DTO.UserResponseDTO;
import com.fitness.application.users.entity.Roles;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponseDTO create(UserRequestDTO userDto){
        User user = userMapper.toEntity(userDto);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    public boolean checkUsername(String username){
        return !userRepository.existsByUsername(username);
    }

    public boolean checkEmail(String email){
        return !userRepository.existsByEmail(email);
    }

    private boolean hasPermition(User user){
        Set<Roles> roles = user.getRoles();
        if (roles.contains(Roles.ADMIN) || roles.contains(Roles.MODERATOR)) {
            return true;
        }
        return false;
    }

    public UserResponseDTO update(User reqUser, UUID uuid, UserRequestDTO data) {
        User saved = getByUuidOrThrow(uuid);

        if (!reqUser.getUuid().equals(saved.getUuid()) && !hasPermition(reqUser)) {
            throw new AccessDeniedExecption("U cant do it bro");
        }

        if (data.getUsername() != null) {
            saved.setUsername(data.getUsername());
        }
        if (data.getEmail() != null) {
            saved.setEmail(data.getEmail());
        }

        return userMapper.toResponseDto(userRepository.save(saved));
    }

    public PageDTO<UserResponseDTO> getAll(int page, int size){
        Page<User> users = userRepository.findAll(Pageable.ofSize(size).withPage(page));
        return new PageDTO<UserResponseDTO>(
            users.getContent().stream().map(userMapper::toResponseDto).toList(), 
            users.getPageable().getPageNumber(), 
            users.getTotalPages()
        );
    }
    
    public UserResponseDTO getByUuid(UUID uuid){
        return userMapper.toResponseDto(getByUuidOrThrow(uuid));
    }

    public void delete(User reqUser, UUID uuid){
        User saved = getByUuidOrThrow(uuid);
        if (!hasPermition(reqUser) && !reqUser.getUuid().equals(saved.getUuid())) {
            throw new AccessDeniedExecption("U cant do it bro");
        }
        userRepository.deleteById(uuid);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
            () -> new UsernameNotFoundException("User not found with username: " + username)
        );
        return new UserDetailsImpl(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getUserOrThrowNotFound(User user){
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    private User getByUuidOrThrow(UUID uuid){
        return userRepository.findById(uuid).orElseThrow(() -> 
                new EntityNotFoundException("User with uuid:"+ uuid +" was not found")
            );
    }
}
