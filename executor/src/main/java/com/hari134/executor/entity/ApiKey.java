package com.hari134.executor.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "api_keys")
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apiKeyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String apiKey;

    @Column(nullable = false)
    private boolean isValid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Long getApiKeyId() {
      return apiKeyId;
    }

    public void setApiKeyId(Long apiKeyId) {
      this.apiKeyId = apiKeyId;
    }

    public User getUser() {
      return user;
    }

    public void setUser(User user) {
      this.user = user;
    }

    public String getApiKey() {
      return apiKey;
    }

    public void setApiKey(String apiKey) {
      this.apiKey = apiKey;
    }

    public boolean getIsValid() {
      return isValid;
    }

    public void setIsValid(boolean isValid) {
      this.isValid = isValid;
    }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
    }
}


