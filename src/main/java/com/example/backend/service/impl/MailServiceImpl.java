package com.example.backend.service.impl;

import com.example.backend.dto.email.EmailRequestDto;
import com.example.backend.dto.email.MessageRequestDto;
import com.example.backend.exception.RegistrationException;
import com.example.backend.model.CheckMailUser;
import com.example.backend.model.MailUser;
import com.example.backend.repository.CheckUserRepository;
import com.example.backend.repository.MailUserRepository;
import com.example.backend.repository.UserRepository;
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
    private static final String REGISTRATION_MESSAGE = "Hello, enter your verification code ";
    private static final String REGISTRATION_SUBJECT = "Registration to smart estate";

    private final JavaMailSender mailSender;
    private final CheckUserRepository checkUserRepository;
    private final MailUserRepository mailUserRepository;
    private final UserRepository userRepository;

    public void sendWelcomeMessage(EmailRequestDto emailRequest) {
        if (mailUserRepository.existsByEmail(emailRequest.getEmail())) {
            throw new RegistrationException(
                    "User " + emailRequest.getEmail() + " already subscribed to mailing list");
        }
        MailUser user = new MailUser();
        user.setEmail(emailRequest.getEmail());
        mailUserRepository.save(user);

        SimpleMailMessage mailMessage = initializeSimpleMailMessage(WELCOME_SUBJECT);
        sendMessage(mailMessage, emailRequest.getEmail(), WELCOME_MESSAGE);
    }

    @Override
    public void sendMessageToAll(MessageRequestDto requestDto) {
        List<MailUser> mailUsers = mailUserRepository.findAll();
        SimpleMailMessage mailMessage = initializeSimpleMailMessage(requestDto.getMailSubject());

        for (MailUser user : mailUsers) {
            sendMessage(mailMessage, user.getEmail(),
                    "Hi, " + user.getEmail() + System.lineSeparator()
                            + requestDto.getMailBody());
        }
    }

    public void sendCheckedCode(EmailRequestDto emailRequest) {
        if (userRepository.existsByEmail(emailRequest.getEmail())) {
            throw new RegistrationException(
                    "User " + emailRequest.getEmail() + " already exist");
        }
        if (checkUserRepository.existsByEmail(emailRequest.getEmail())) {
            throw new RegistrationException(
                    "User " + emailRequest.getEmail() + " already exist");
        }
        CheckMailUser user = new CheckMailUser();
        user.setEmail(emailRequest.getEmail());
        user.setCheckedNumber((int)(Math.random() * 900000) + 100000);
        checkUserRepository.save(user);

        SimpleMailMessage mailMessage = initializeSimpleMailMessage(REGISTRATION_SUBJECT);
        sendMessage(mailMessage, emailRequest.getEmail(),
                REGISTRATION_MESSAGE + user.getCheckedNumber());
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
