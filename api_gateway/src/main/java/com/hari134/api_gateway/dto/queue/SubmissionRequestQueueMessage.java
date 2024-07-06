package com.hari134.api_gateway.dto.queue;

import com.hari134.api_gateway.dto.judge.ExecutionConfig;
import java.io.Serializable;

public class SubmissionRequestQueueMessage implements Serializable{

    private String correlationId;
    private ExecutionConfig executionConfig;

    public SubmissionRequestQueueMessage(String correlationId,ExecutionConfig executionConfig){
        this.correlationId = correlationId;
        this.executionConfig = executionConfig;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public ExecutionConfig getExecutionConfig() {
        return executionConfig;
    }

    public void setExecutionConfig(ExecutionConfig executionConfig) {
        this.executionConfig = executionConfig;
    }

}
