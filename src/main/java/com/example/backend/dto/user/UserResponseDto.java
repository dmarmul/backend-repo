package com.example.backend.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String token;
}
