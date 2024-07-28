package com.hari134.api_gateway.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hari134.api_gateway.entity.ApiKey;
import com.hari134.api_gateway.entity.User;
import com.hari134.api_gateway.repository.ApiKeyRepository;
import com.hari134.api_gateway.repository.UserRepository;

@Service
public class ApiKeyService {

  @Autowired
  private ApiKeyRepository apiKeyRepository;

  @Autowired
  private UserRepository userRepository;

  @Transactional
  public String generateApiKey(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

    String newApiKey = generateUniqueKey();
    ApiKey apiKey = new ApiKey();
    apiKey.setApiKey(newApiKey);
    apiKey.setUser(user);
    apiKey.setIsValid(true);
    apiKey.setCreatedAt(LocalDateTime.now());

    apiKeyRepository.save(apiKey);
    return newApiKey;
  }

  public boolean validateApiKey(String apiKey) {
    Optional<ApiKey> apiKeyOptional = apiKeyRepository.findByApiKey(apiKey);
    return apiKeyOptional.map(ApiKey::getIsValid).orElse(false);
  }

  public User getUserFromApiKey(String apiKey) {
    ApiKey key = apiKeyRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new IllegalArgumentException("API Key not found"));
    return key.getUser();
  }

  public void invalidateApiKey(String apiKey) {
    ApiKey key = apiKeyRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new IllegalArgumentException("API Key not found"));
    if (key != null) {
      key.setIsValid(false);
      apiKeyRepository.save(key);
    }
  }

  public boolean isApiKeyValid(String apiKey) {
    ApiKey key = apiKeyRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new IllegalArgumentException("API Key not found"));
    return key != null && key.getIsValid();
  }

  public List<ApiKey> getApiKeysForUser(Long userId) {
    return apiKeyRepository.findAllByUserId(userId);
  }

  private static String generateUniqueKey() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
}
