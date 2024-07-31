package com.hari134.api_gateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hari134.api_gateway.entity.EmailAuth;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

    @Query("SELECT ea FROM EmailAuth ea WHERE ea.user.userId= :userId")
    EmailAuth findByUserId(Long userId);

    Optional<EmailAuth> findByEmail(String email);
}

