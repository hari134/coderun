package com.hari134.api_gateway.service.impl;

import com.hari134.api_gateway.dto.queue.SubmissionResponseQueueMessage;
import com.hari134.api_gateway.entity.ApiKey;
import com.hari134.api_gateway.entity.CodeSubmission;
import com.hari134.api_gateway.entity.User;
import com.hari134.api_gateway.repository.CodeSubmissionRepository;
import com.hari134.api_gateway.repository.ApiKeyRepository;
import com.hari134.api_gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CodeSubmissionService {

    @Autowired
    private CodeSubmissionRepository codeSubmissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public void saveSubmissionResponse(SubmissionResponseQueueMessage response, String correlationId, Long userId,
            Long apiKeyId,
            String sourceCode, String stdin, String expectedOutput,
            String memoryLimit, String cpuTimeLimit, String wallTimeLimit) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid API key ID"));

        CodeSubmission submission = new CodeSubmission();
        submission.setCorrelationId(correlationId);
        submission.setUser(user);
        submission.setApiKey(apiKey);
        submission.setSourceCode(sourceCode);
        submission.setStdin(stdin);
        submission.setStdout(response.getOutput());
        submission.setExpectedOutput(expectedOutput);
        submission.setMemoryLimit(Float.parseFloat(memoryLimit));
        submission.setCpuTimeLimit(Float.parseFloat(cpuTimeLimit));
        submission.setWallTimeLimit(Float.parseFloat(wallTimeLimit));
        submission.setMemory(Float.parseFloat(response.getMemoryUsed()));
        submission.setCpuTime(Float.parseFloat(response.getCpuTime()));
        submission.setWallTime(Float.parseFloat(response.getWallTime()));
        submission.setExitStatus(Integer.parseInt(response.getExitCode()));
        submission.setMessage(response.getStatus());
        submission.setSubmissionComplete(true);
        submission.setSubmissionTime(LocalDateTime.now());

        codeSubmissionRepository.save(submission);
    }

    public void saveOnAsyncSubmissionRequestInitialization(String correlationId, Long userId, Long apiKeyId,
            String sourceCode, String stdin, String expectedOutput,
             String memoryLimit, String cpuTimeLimit, String wallTimeLimit) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid API key ID"));

        CodeSubmission submission = new CodeSubmission();
        submission.setCorrelationId(correlationId);
        submission.setUser(user);
        submission.setApiKey(apiKey);
        submission.setSourceCode(sourceCode);
        submission.setStdin(stdin);
        submission.setExpectedOutput(expectedOutput);
        submission.setMemoryLimit(Float.parseFloat(memoryLimit));
        submission.setCpuTimeLimit(Float.parseFloat(cpuTimeLimit));
        submission.setWallTimeLimit(Float.parseFloat(wallTimeLimit));
        submission.setSubmissionComplete(false);
        submission.setSubmissionTime(LocalDateTime.now());

        codeSubmissionRepository.save(submission);
    }

    public Optional<CodeSubmission> findByCorrelationId(String correlationId){
        return codeSubmissionRepository.findByCorrelationId(correlationId);
    }
}
