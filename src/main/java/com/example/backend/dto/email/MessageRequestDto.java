package com.example.backend.dto.email;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MessageRequestDto {
    @NotBlank
    private String mailSubject;
    @NotBlank
    private String mailBody;
}
