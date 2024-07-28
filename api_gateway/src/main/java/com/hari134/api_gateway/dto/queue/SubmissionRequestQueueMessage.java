package com.hari134.api_gateway.dto.queue;

import java.io.Serializable;

import com.hari134.api_gateway.dto.api.requests.SubmissionRequest;

public class SubmissionRequestQueueMessage implements Serializable{
    private String language;
    private String code;
    private String timeLimit;
    private String memoryLimit;
    private String stdin;
    private String expectedOutput;
    private Boolean wait;
    private String correlationId;

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Boolean getWait() {
        return wait;
    }

    public void setWait(Boolean wait) {
        this.wait = wait;
    }

    public String getStdin() {
        return stdin;
    }

    public void setStdin(String stdin) {
        this.stdin = stdin;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(String memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public static SubmissionRequestQueueMessage fromSubmissionRequest(SubmissionRequest submissionRequest) {
        SubmissionRequestQueueMessage submissionRequestQueueMessage = new SubmissionRequestQueueMessage();
        submissionRequestQueueMessage.setCode(submissionRequest.getSourceCode());
        submissionRequestQueueMessage.setExpectedOutput(submissionRequest.getExpectedOutput());
        submissionRequestQueueMessage.setLanguage(submissionRequest.getLanguage());
        submissionRequestQueueMessage.setMemoryLimit(submissionRequest.getMemoryLimit());
        submissionRequestQueueMessage.setStdin(submissionRequest.getStdInput());
        submissionRequestQueueMessage.setWait(submissionRequest.getWait());
        submissionRequestQueueMessage.setTimeLimit(submissionRequest.getTimeLimit());
        return submissionRequestQueueMessage;
    }
}
