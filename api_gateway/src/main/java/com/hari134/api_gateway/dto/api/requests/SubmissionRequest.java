package com.hari134.api_gateway.dto.api.requests;

public class SubmissionRequest {
    private String language;
    private String sourceCode;
    private String stdInput;
    private String expectedOutput;
    private String timeLimit;
    private String wallTimeLimit;
    private String memoryLimit;

    public String getWallTimeLimit() {
        return wallTimeLimit;
    }

    public void setWallTimeLimit(String wallTimeLimit) {
        this.wallTimeLimit = wallTimeLimit;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    @Override
    public String toString() {
        return "SubmissionRequest{" +
                "language='" + language + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                ", stdInput='" + stdInput + '\'' +
                ", expectedOutput='" + expectedOutput + '\'' +
                ", timeLimit='" + timeLimit + '\'' +
                ", memoryLimit='" + memoryLimit + '\'' +
                '}';
    }
}
