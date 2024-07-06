package com.hari134.coderun.dto.api;

public class SubmissionRequest {
    private Long languageId;
    private String sourceCode;
    private String stdInput;
    private String expectedOutput;
    private Float timeLimit;
    private Float memoryLimit;

    public Long getLanguageId() {
        return languageId;
    }
    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
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
