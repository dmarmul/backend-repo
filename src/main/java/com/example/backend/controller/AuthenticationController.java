package com.example.backend.controller;

import com.example.backend.dto.user.UserLoginRequestDto;
import com.example.backend.dto.user.UserRegistrationRequestDto;
import com.example.backend.dto.user.UserResponseDto;
import com.example.backend.exception.RegistrationException;
import com.example.backend.security.AuthenticationService;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/log-in")
    @Operation(summary = "Login user",
            description = "Login user. Email must be already registered. "
                    + "Password must be valid. Return JWT token to client")
    public UserResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return userService.authenticate(requestDto);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add user",
            description = "Register a new user. Email must be unique. "
                    + "Password size must be from 6 to 30 symbols. ShippingAddress can be null.")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        UserResponseDto userResponseDto = userService.register(requestDto);
        userResponseDto.setToken(authenticationService.authenticate(
                requestDto.getEmail(), requestDto.getPassword()));
        return userResponseDto;
    }
}
