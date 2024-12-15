package com.example.backend.dto.email;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailRequestDto {
    @Email(message = "Invalid email address")
    private String emailTo;
}
