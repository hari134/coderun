package com.hari134.executor.dto.queue;



import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SubmissionResponseQueueMessage {

  private String output;
  private String error;
  @SerializedName("isTLE")
  private boolean isTLE;
  @SerializedName("isMLE")
  private boolean isMLE;
  private String cpu_time;
  private String memory_used;
  private String output_match;
  private String status;
  private String correlationId;
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

  public static SubmissionResponseQueueMessage fromJson(String jsonString,String stdErr,String correlationId) {
    Gson gson = new Gson();
    SubmissionResponseQueueMessage submissionResponseQueueMessage = gson.fromJson(jsonString, SubmissionResponseQueueMessage.class);
    submissionResponseQueueMessage.setError(stdErr);
    submissionResponseQueueMessage.setCorrelationId(correlationId);
    return submissionResponseQueueMessage;
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
        ", status='" + status+ '\'' +
        ", correlationId='" + correlationId+ '\'' +
        '}';
  }
}
