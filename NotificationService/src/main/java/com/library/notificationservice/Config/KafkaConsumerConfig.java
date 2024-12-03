package com.library.notificationservice.Config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.notificationservice.Dto.SendEmailMessageDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import javax.mail.Session;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

@Configuration
public class KafkaConsumerConfig {
    private ObjectMapper objectMapper;
    private EmailUtil emailUtil;

    public KafkaConsumerConfig(ObjectMapper objectMapper, EmailUtil emailUtil) {
        this.objectMapper = objectMapper;
        this.emailUtil = emailUtil;
    }

    //code to listen an event kafka should come
    @KafkaListener(topics = "sendemail", groupId = "notificationService")
    public void handleSendEmailEvent(String message) throws JsonProcessingException {
        //code to send an email to the user
        SendEmailMessageDto sendEmailMessageDto = objectMapper.readValue(
                message,
                SendEmailMessageDto.class);
        System.out.println("Email sent to "+ sendEmailMessageDto.getTo());

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        javax.mail.Session session = Session.getInstance(props, auth);

        emailUtil.sendEmail(session, sendEmailMessageDto.getTo(),"TLSEmail Testing Subject", "TLSEmail Testing Body");



    }
    
}
