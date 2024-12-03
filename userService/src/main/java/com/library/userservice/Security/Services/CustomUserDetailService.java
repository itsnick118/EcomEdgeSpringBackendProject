package com.library.userservice.Security.Services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.library.userservice.Models.User;
import com.library.userservice.Repository.UserRepository;
import com.library.userservice.Security.SecurityModels.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@JsonDeserialize
public class CustomUserDetailService implements UserDetailsService{
    private UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser= userRepository.findByEmail(username);
        if(optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with email: "+username+" does not found");
        }
        CustomUserDetails customUserDetails= new CustomUserDetails(optionalUser.get());
        return customUserDetails;
    }
}
