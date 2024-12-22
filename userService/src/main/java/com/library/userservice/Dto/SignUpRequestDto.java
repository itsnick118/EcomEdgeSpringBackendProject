package com.library.userservice.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String email;
    private String name;
    private String password;
    private String role;
}
