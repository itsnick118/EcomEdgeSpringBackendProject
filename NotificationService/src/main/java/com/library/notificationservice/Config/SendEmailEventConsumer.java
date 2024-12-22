package com.library.notificationservice.Config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.notificationservice.Dto.SendEmailMessageDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SendEmailEventConsumer {
    private ObjectMapper objectMapper;

    public SendEmailEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleSendEmailEvent(String message) throws JsonProcessingException {
        try {
            SendEmailMessageDto sendEmailDto = objectMapper.readValue(
                    message,
                    SendEmailMessageDto.class
            );

            String to = sendEmailDto.getTo();
            String subject = sendEmailDto.getSubject();
            String body = sendEmailDto.getBody();

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("manojbhosale1589@gmail.com", "iftiwbewdxmhutqs");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, to, subject, body);
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }
    }
}