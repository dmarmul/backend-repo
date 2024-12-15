package com.example.backend.controller;

import com.example.backend.dto.email.EmailRequestDto;
import com.example.backend.dto.email.MessageRequestDto;
import com.example.backend.service.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mail distribution", description = "Endpoints for manage mail distribution")
@RequiredArgsConstructor
@RestController
@RequestMapping("sendmail")
public class MailSenderController {
    private final MailService mailService;

    @PostMapping("/welcome")
    public void sendWelcomeMessage(@Valid @RequestBody EmailRequestDto requestDto) {
        mailService.sendWelcomeMessage(requestDto);
    }

    @PostMapping("/all")
    public void sendMessageToAll(@Valid @RequestBody MessageRequestDto requestDto) {
        mailService.sendMessageToAll(requestDto);
    }
}
