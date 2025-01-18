package com.example.backend.service;

import com.example.backend.dto.house.ContactedHousePageDto;
import com.example.backend.dto.house.LickedHousePageDto;
import com.example.backend.dto.user.UserLoginRequestDto;
import com.example.backend.dto.user.UserRegistrationRequestDto;
import com.example.backend.dto.user.UserResponseDto;
import com.example.backend.model.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    UserResponseDto authenticate(@Valid UserLoginRequestDto requestDto);

    LickedHousePageDto findAllLikedHouses(User user, Pageable pageable);

    ContactedHousePageDto findAllUserContacts(User user, Pageable pageable);
}
