package com.library.userservice.Exceptions;

import com.library.userservice.Dto.ExceptionDto;
import com.library.userservice.Dto.UserNotFoundDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserNotFoundException> handleUserNotFoundException(UserNotFoundException userNotFoundException){
        UserNotFoundDto userNotFoundDto = new UserNotFoundDto();
        userNotFoundDto.setMessage("User with email " + userNotFoundException.getEmail() + " not found");
        return new ResponseEntity(userNotFoundDto, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionDto> handleInvalidPasswordException(InvalidPasswordException ex) {
        ExceptionDto errorDetails = new ExceptionDto();
        errorDetails.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleGlobalException(Exception ex, WebRequest request) {
        ExceptionDto errorDetails = new ExceptionDto();
        errorDetails.setMessage(ex.getMessage());
        errorDetails.setResolution(request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
