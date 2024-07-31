package com.hari134.api_gateway.api.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.hari134.api_gateway.dto.api.requests.LoginRequest;
import com.hari134.api_gateway.dto.api.requests.UserRegistrationRequest;
import com.hari134.api_gateway.dto.api.responses.ApiResponse;
import com.hari134.api_gateway.entity.ApiKey;
import com.hari134.api_gateway.entity.User;
import com.hari134.api_gateway.service.impl.UserService;
import com.hari134.api_gateway.service.impl.ApiKeyService;
import com.hari134.api_gateway.service.impl.CustomUserDetailsService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationDto) {
        try {
            User user = userService.registerUser(registrationDto);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("User logged in successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    @GetMapping("/generate-api-key")
    public ResponseEntity<?> generateApiKey() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "User not authenticated", null));
        }

        CustomUserDetailsService.CustomUserDetails userDetails = (CustomUserDetailsService.CustomUserDetails) authentication
                .getPrincipal();
        Long userId = userDetails.getUserId();

        try {
            String apiKey = apiKeyService.generateApiKey(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "API Key generated successfully", apiKey));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/get-user-api-keys")
    public ResponseEntity<?> getUserApiKeys() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "User not authenticated", null));
        }

        CustomUserDetailsService.CustomUserDetails userDetails = (CustomUserDetailsService.CustomUserDetails) authentication
                .getPrincipal();
        Long userId = userDetails.getUserId();

        try {
            List<ApiKey> userApiKeys = apiKeyService.getApiKeysForUser(userId);
            List<Map<String, Object>> response = userApiKeys.stream().map(apiKey -> {
                Map<String, Object> apiKeyInfo = new HashMap<>();
                apiKeyInfo.put("apiKey", apiKey.getApiKey());
                apiKeyInfo.put("isValid", apiKey.getIsValid());
                apiKeyInfo.put("createdAt", apiKey.getCreatedAt());
                return apiKeyInfo;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>(true, "Api keys retrieved successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

}
