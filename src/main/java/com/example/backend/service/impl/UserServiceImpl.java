package com.example.backend.service.impl;

import com.example.backend.dto.UserLoginRequestDto;
import com.example.backend.dto.UserRegistrationRequestDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.exception.RegistrationException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.AuthenticationService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "User " + requestDto.getEmail() + " already exist");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRole(Role.RoleType.ROLE_USER)));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto authenticate(UserLoginRequestDto requestDto) {
        String token = authenticationService.authenticate(
                requestDto.email(), requestDto.password());
        User user = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by email: " + requestDto.email()));
        UserResponseDto dto = userMapper.toDto(user);
        dto.setToken(token);
        return dto;
    }
}
