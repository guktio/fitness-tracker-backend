package com.fitness.application.users;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fitness.application.security.UserDetailsImpl;
import com.fitness.application.users.DTO.UserRequestDTO;
import com.fitness.application.users.DTO.UserResponseDTO;
import com.fitness.application.users.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponseDTO create(UserRequestDTO userDto){
        User user = userMapper.toEntity(userDto);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    public boolean checkUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public UserResponseDTO update(UserRequestDTO newUser, UUID uuid) {
        User saved = userRepository.findById(uuid).orElseThrow(() ->
            new EntityNotFoundException("User with uuid: " + uuid + " was not found")
        );

        if (newUser.getUsername() != null) {
            saved.setUsername(newUser.getUsername());
        }
        if (newUser.getEmail() != null) {
            saved.setEmail(newUser.getEmail());
        }
        return userMapper.toResponseDto(userRepository.save(saved));
    }

    public Page<UserResponseDTO> getAll(int page, int size){
        return userRepository.findAll(Pageable.ofSize(size).withPage(page))
            .map(userMapper::toResponseDto);
    }
    
    public UserResponseDTO getByUuid(UUID uuid){
        return userMapper.toResponseDto(
            userRepository.findById(uuid).orElseThrow(() -> 
                new EntityNotFoundException("User with uuid:"+ uuid +" was not found")
            )
        );
    }

    public void delete(UUID uuid){
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
}
