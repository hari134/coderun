package com.hari134.api_gateway.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "code_submissions")
public class CodeSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "api_key_id", nullable = false)
    private ApiKey apiKey;

    @Column(nullable = false)
    private boolean isSubmissionComplete;

    @Column(nullable = false)
    private LocalDateTime submissionTime;

    @Column
    private String sourceCode;

    @Column
    private String stdin;

    @Column
    private String stdout;

    @Column
    private String expectedOutput;

    @Column
    private float timeLimit;

    @Column
    private float memoryLimit;

    @Column
    private float cpuTimeLimit;

    @Column
    private float wallTimeLimit;

    @Column
    private float memory;

    @Column
    private float cpuTime;

    @Column
    private float wallTime;

    @Column
    private int exitStatus;

    @Column
    private String message;

    public Long getSubmissionId() {
      return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
      this.submissionId = submissionId;
    }

    public User getUser() {
      return user;
    }

    public void setUser(User user) {
      this.user = user;
    }

    public ApiKey getApiKey() {
      return apiKey;
    }

    public void setApiKey(ApiKey apiKey) {
      this.apiKey = apiKey;
    }

    public boolean isSubmissionComplete() {
      return isSubmissionComplete;
    }

    public void setSubmissionComplete(boolean isSubmissionComplete) {
      this.isSubmissionComplete = isSubmissionComplete;
    }

    public LocalDateTime getSubmissionTime() {
      return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
      this.submissionTime = submissionTime;
    }

    public String getSourceCode() {
      return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
      this.sourceCode = sourceCode;
    }

    public String getStdin() {
      return stdin;
    }

    public void setStdin(String stdin) {
      this.stdin = stdin;
    }

    public String getStdout() {
      return stdout;
    }

    public void setStdout(String stdout) {
      this.stdout = stdout;
    }

    public String getExpectedOutput() {
      return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
      this.expectedOutput = expectedOutput;
    }

    public float getTimeLimit() {
      return timeLimit;
    }

    public void setTimeLimit(float timeLimit) {
      this.timeLimit = timeLimit;
    }

    public float getMemoryLimit() {
      return memoryLimit;
    }

    public void setMemoryLimit(float memoryLimit) {
      this.memoryLimit = memoryLimit;
    }

    public float getCpuTimeLimit() {
      return cpuTimeLimit;
    }

    public void setCpuTimeLimit(float cpuTimeLimit) {
      this.cpuTimeLimit = cpuTimeLimit;
    }

    public float getWallTimeLimit() {
      return wallTimeLimit;
    }

    public void setWallTimeLimit(float wallTimeLimit) {
      this.wallTimeLimit = wallTimeLimit;
    }

    public float getMemory() {
      return memory;
    }

    public void setMemory(float memory) {
      this.memory = memory;
    }

    public float getCpuTime() {
      return cpuTime;
    }

    public void setCpuTime(float cpuTime) {
      this.cpuTime = cpuTime;
    }

    public float getWallTime() {
      return wallTime;
    }

    public void setWallTime(float wallTime) {
      this.wallTime = wallTime;
    }

    public int getExitStatus() {
      return exitStatus;
    }

    public void setExitStatus(int exitStatus) {
      this.exitStatus = exitStatus;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
}

