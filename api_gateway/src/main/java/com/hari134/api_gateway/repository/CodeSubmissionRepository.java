package com.hari134.api_gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hari134.api_gateway.entity.CodeSubmission;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Long> {
    List<CodeSubmission> findBySubmissionId(Long userId);

    @Query("SELECT cs FROM CodeSubmission cs WHERE cs.apiKey.apiKeyId = :apiKeyId")
    List<CodeSubmission> findByApiKeyId(@Param("apiKeyId") Long apiKeyId);

    @Query("SELECT cs FROM CodeSubmission cs WHERE cs.correlationId= :correlationId")
    Optional<CodeSubmission> findByCorrelationId(@Param("correlationId") String correlationId);
}
