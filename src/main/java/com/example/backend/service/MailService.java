package com.example.backend.service;

import com.example.backend.dto.email.EmailRequestDto;
import com.example.backend.dto.email.MessageRequestDto;

public interface MailService {
    void sendWelcomeMessage(EmailRequestDto emailRequest);

    void sendMessageToAll(MessageRequestDto requestDto);
}
