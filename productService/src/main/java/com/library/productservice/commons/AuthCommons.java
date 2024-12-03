package com.library.productservice.commons;

import com.library.productservice.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthCommons {
    private RestTemplate restTemplate;

    public AuthCommons(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDto validateToken(String tokenValue){
        ResponseEntity<UserDto> responseEntity=
                restTemplate.getForEntity(
                        "http://localhost:4142/users/validate/"+ tokenValue,
                        UserDto.class
                );
        if (responseEntity.getBody()== null){
            //token is invalid
            //throw some exception here.
            return null;
        }
        return responseEntity.getBody();

    }
}
