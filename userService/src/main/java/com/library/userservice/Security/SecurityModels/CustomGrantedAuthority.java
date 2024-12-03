package com.library.userservice.Security.SecurityModels;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.library.userservice.Models.Role;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority
{
    private String authority;

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getValue();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
