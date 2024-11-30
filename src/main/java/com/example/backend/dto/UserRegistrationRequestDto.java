package com.example.backend.dto;

import com.example.backend.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, max = 30)
    private String password;
    @NotBlank
    @Size(min = 6, max = 30)
    private String confirmPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
