package com.library.productservice.dto;

import com.library.productservice.Models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private List<Role> roles;
}
