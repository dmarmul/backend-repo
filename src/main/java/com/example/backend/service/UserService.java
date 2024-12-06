package com.example.backend.service;

import com.example.backend.dto.UserLoginRequestDto;
import com.example.backend.dto.UserRegistrationRequestDto;
import com.example.backend.dto.UserResponseDto;
import jakarta.validation.Valid;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    UserResponseDto authenticate(@Valid UserLoginRequestDto requestDto);
}
