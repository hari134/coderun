package com.hari134.api_gateway.dto.queue;

public class SubmissionResponseQueueMessage {

  private String output;
  private String error;
  private boolean isTLE;
  private boolean isMLE;
  private String cpu_time;
  private String memory_used;
  private String output_match;
  private String status;
  private String correlationId;
  private String wall_time;
  private String exit_code;

  // Getters and Setters

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public boolean isTLE() {
    return isTLE;
  }

  public void setTLE(boolean isTLE) {
    this.isTLE = isTLE;
  }

  public boolean isMLE() {
    return isMLE;
  }

  public void setMLE(boolean isMLE) {
    this.isMLE = isMLE;
  }

  public String getCpuTime() {
    return cpu_time;
  }

  public void setCpuTime(String cpu_time) {
    this.cpu_time = cpu_time;
  }

  public String getMemoryUsed() {
    return memory_used;
  }

  public void setMemoryUsed(String memory_used) {
    this.memory_used = memory_used;
  }

  public String getOutputMatch() {
    return output_match;
  }

  public void setOutputMatch(String output_match) {
    this.output_match = output_match;
  }

  public String getWallTime() {
    return wall_time;
  }

  public void setWallTime(String wall_time) {
    this.wall_time = wall_time;
  }

  public String getExitCode() {
    return exit_code;
  }

  public void setExitCode(String exit_code) {
    this.exit_code = exit_code;
  }

  @Override
  public String toString() {
    return "ExecutionResult{" +
        "output='" + output + '\'' +
        ", error='" + error + '\'' +
        ", isTLE=" + isTLE +
        ", isMLE=" + isMLE +
        ", cpu_time='" + cpu_time + '\'' +
        ", memory_used='" + memory_used + '\'' +
        ", output_match='" + output_match + '\'' +
        ", status='" + status + '\'' +
        ", correlationId='" + correlationId + '\'' +
        ", wall_time='" + wall_time + '\'' +
        ", exit_code='" + exit_code + '\'' +
        '}';
  }
}