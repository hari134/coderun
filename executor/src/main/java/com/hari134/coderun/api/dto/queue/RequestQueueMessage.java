package com.hari134.coderun.api.dto.queue;

import java.io.Serializable;

public class RequestQueueMessage implements Serializable{

    private String correlationId;
    private String code;
    private String language;

    public String getCorrelationId() {
        return this.correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RequestQueueMessage(String correlationId,String language,String code){
        this.correlationId = correlationId;
        this.language = language;
        this.code = code;
    }
}
