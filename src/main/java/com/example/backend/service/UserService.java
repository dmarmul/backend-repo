package com.example.backend.service;

import com.example.backend.dto.UserRegistrationRequestDto;
import com.example.backend.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
