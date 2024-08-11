package com.hari134.api_gateway.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hari134.api_gateway.dto.api.requests.UserRegistrationRequest;
import com.hari134.api_gateway.entity.EmailAuth;
import com.hari134.api_gateway.entity.User;
import com.hari134.api_gateway.repository.EmailAuthRepository;
import com.hari134.api_gateway.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegistrationRequest registrationDto) {
        // Check if the email already exists
        if (emailAuthRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Proceed with user creation if the email is not already in use
        User user = new User();
        user.setName(registrationDto.getName());
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        // Create and save EmailAuth entry
        EmailAuth emailAuth = new EmailAuth();
        emailAuth.setUser(user);
        emailAuth.setEmail(registrationDto.getEmail());
        emailAuth.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        emailAuth.setCreatedAt(LocalDateTime.now());
        emailAuthRepository.save(emailAuth);

        return user;
    }
}
