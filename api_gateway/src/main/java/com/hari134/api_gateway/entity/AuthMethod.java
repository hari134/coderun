package com.hari134.api_gateway.entity;

import javax.persistence.*;

@Entity
@Table(name = "auth_methods")
public class AuthMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String provider;  // "local", "google"

    @Column(nullable = true)
    private String password;  // Hashed password for local, null for OAuth

    @Column(nullable = true)
    private String providerId;  // ID from the OAuth provider, null for local

    @Column(nullable = true)
    private String token;  // Access token for OAuth, null for local

}
