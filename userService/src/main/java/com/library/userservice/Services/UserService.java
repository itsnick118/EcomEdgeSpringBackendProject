package com.library.userservice.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.userservice.Configs.KafkaProducerConfig;
import com.library.userservice.Dto.SendEmailMessageDto;
import com.library.userservice.Exceptions.InvalidPasswordException;
import com.library.userservice.Exceptions.InvalidTokenException;
import com.library.userservice.Exceptions.TokenNotFoundException;
import com.library.userservice.Exceptions.UserNotFoundException;
import com.library.userservice.Models.Token;
import com.library.userservice.Models.User;
import com.library.userservice.Repository.TokenRepository;
import com.library.userservice.Repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, TokenRepository tokenRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public User signUp(String email,
                       String name,
                       String password,
                       String role) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);

        User savedUser= userRepository.save(user);

        SendEmailMessageDto sendEmailMessageDto= new SendEmailMessageDto();
        sendEmailMessageDto.setTo(savedUser.getEmail());
        sendEmailMessageDto.setSubject("Registration successful");
        sendEmailMessageDto.setFrom(savedUser.getEmail());
        sendEmailMessageDto.setBody("User with email "+ savedUser.getEmail() + " registered successfully");

        try {
            kafkaTemplate.send(
                    "sendEmail",
                    objectMapper.writeValueAsString(sendEmailMessageDto));
        } catch (Exception e) {
            System.out.println("Exception occurred while sending email");
        }
        return savedUser;
    }

    public Token login(String email,
                       String password) throws UserNotFoundException {
        Optional<User> optionalUser= userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User with email "+ email + " doesn't exist", email);
        }
        User user =optionalUser.get();
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Invalid password for user with email " + email);
        }
        //login successful, generate a token.
        Token token= generateToken(user);
        Token savedToken= tokenRepository.save(token);
        return savedToken;
    }

    private Token generateToken(User user){
        LocalDate currentDate= LocalDate.now();
        LocalDate thirtyDaysLater= currentDate.plusDays(30);
        Date expiryDate= Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Token token= new Token();
        token.setExpiryAt(expiryDate);
        //128 character alphanumeric string.
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        return token;
    }

    public void logout(String token) {
        Optional<Token> optionalToken= tokenRepository.findByValueAndDeleted(token, false);
        if (optionalToken.isEmpty()) {
            throw new TokenNotFoundException("Token not found or already deleted");
        }
        Token tokenValue= optionalToken.get();
        tokenValue.setDeleted(true);
        tokenRepository.save(tokenValue);

    }

    public User validateToken(String token) {
        Optional<Token> optionalToken= tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date());
        if (optionalToken.isEmpty()) {
            throw new InvalidTokenException("Token is invalid or expired");
        }
        return optionalToken.get().getUser();
    }

}
