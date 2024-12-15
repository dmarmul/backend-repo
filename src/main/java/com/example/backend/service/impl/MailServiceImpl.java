package com.example.backend.service.impl;

import com.example.backend.dto.email.EmailRequestDto;
import com.example.backend.dto.email.MessageRequestDto;
import com.example.backend.exception.RegistrationException;
import com.example.backend.model.MailUser;
import com.example.backend.repository.MailUserRepository;
import com.example.backend.service.MailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private static final String WELCOME_MESSAGE = "Hello, we welcome you to smart estate)";
    private static final String WELCOME_SUBJECT = "welcome to smart estate";

    private final JavaMailSender mailSender;
    private final MailUserRepository userRepository;

    public void sendWelcomeMessage(EmailRequestDto emailRequest) {
        if (userRepository.existsByEmail(emailRequest.getEmailTo())) {
            throw new RegistrationException(
                    "User " + emailRequest.getEmailTo() + " already subscribed to mailing list ");
        }
        MailUser user = new MailUser();
        user.setEmail(emailRequest.getEmailTo());
        userRepository.save(user);

        SimpleMailMessage mailMessage = initializeSimpleMailMessage(WELCOME_SUBJECT);
        sendMessage(mailMessage, emailRequest.getEmailTo(), WELCOME_MESSAGE);
    }

    @Override
    public void sendMessageToAll(MessageRequestDto requestDto) {
        List<MailUser> mailUsers = userRepository.findAll();
        SimpleMailMessage mailMessage = initializeSimpleMailMessage(requestDto.getMailSubject());

        for (MailUser user : mailUsers) {
            sendMessage(mailMessage, user.getEmail(),
                    "Hi, " + user.getEmail() + System.lineSeparator()
                            + requestDto.getMailBody());
        }
    }

    private void sendMessage(SimpleMailMessage mailMessage, String emailTo, String text) {
        mailMessage.setTo(emailTo);
        mailMessage.setText(text);

        mailSender.send(mailMessage);
    }

    private SimpleMailMessage initializeSimpleMailMessage(String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setSubject(subject);

        return mailMessage;
    }
}
