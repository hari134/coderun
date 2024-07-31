package com.hari134.executor.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hari134.executor.dto.queue.SubmissionResponseQueueMessage;
import com.hari134.executor.entity.CodeSubmission;
import com.hari134.executor.repository.CodeSubmissionRepository;


@Service
public class CodeSubmissionService {

    @Autowired
    private CodeSubmissionRepository codeSubmissionRepository;


    public void saveOnAsyncRequestCompletion(String correlationId, SubmissionResponseQueueMessage response) {
        // Fetch the incomplete submission record from the database
        CodeSubmission submission = codeSubmissionRepository.findByCorrelationId(correlationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid correlation ID"));

        // Update the submission details
        submission.setStdout(response.getOutput());
        submission.setMemory(Float.parseFloat(response.getMemoryUsed()));
        submission.setCpuTime(Float.parseFloat(response.getCpuTime()));
        submission.setWallTime(Float.parseFloat(response.getWallTime()));
        submission.setExitStatus(Integer.parseInt(response.getExitCode()));
        submission.setMessage(response.getStatus());
        submission.setSubmissionComplete(true);

        // Save the updated submission record to the database
        codeSubmissionRepository.save(submission);
    }
}
