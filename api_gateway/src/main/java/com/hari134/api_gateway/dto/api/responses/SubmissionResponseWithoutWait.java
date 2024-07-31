package com.hari134.api_gateway.dto.api.responses;

public class SubmissionResponseWithoutWait {
  private String correlationId;

  public SubmissionResponseWithoutWait(String correlationId){
    this.correlationId = correlationId;
  }
  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
