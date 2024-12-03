package com.library.userservice.Exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception{
    private String email;
    private String message;

    public UserNotFoundException(String message, String email) {
        super(message);
        this.email= email;
    }
}
