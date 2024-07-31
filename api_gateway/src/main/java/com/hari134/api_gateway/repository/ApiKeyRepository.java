package com.hari134.api_gateway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hari134.api_gateway.entity.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByApiKey(String apiKey);  

    @Query("SELECT a FROM ApiKey a WHERE a.user.userId = :userId")
    List<ApiKey> findAllByUserId(@Param("userId") Long userId);
}
