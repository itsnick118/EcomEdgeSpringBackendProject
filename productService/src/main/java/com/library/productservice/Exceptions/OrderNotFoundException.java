package com.library.productservice.Exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends Exception{
    private Long id;
    private String message;

    public OrderNotFoundException(String message, Long id) {
        super(message);
        this.id= id;
    }
}