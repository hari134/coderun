package com.hari134.api_gateway.dto.api;

public class SubmissionRequest {
    private String language;
    private String sourceCode;
    private String stdInput;
    private String expectedOutput;
    private Float timeLimit;
    private Float memoryLimit;

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language= language;
    }
    public String getSourceCode() {
        return sourceCode;
    }
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
    public String getStdInput() {
        return stdInput;
    }
    public void setStdInput(String stdInput) {
        this.stdInput = stdInput;
    }
    public String getExpectedOutput() {
        return expectedOutput;
    }
    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
    public Float getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(Float timeLimit) {
        this.timeLimit = timeLimit;
    }
    public Float getMemoryLimit() {
        return memoryLimit;
    }
    public void setMemoryLimit(Float memoryLimit) {
        this.memoryLimit = memoryLimit;
    }
}

