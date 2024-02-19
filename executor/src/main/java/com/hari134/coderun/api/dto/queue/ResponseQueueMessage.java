package com.hari134.coderun.api.dto.queue;

import java.io.Serializable;

public class ResponseQueueMessage implements Serializable{
    private String StdOut;
    private String StdErr;
    private String CorrelationId;
    private String Error;

    public String getStdOut() {
        return this.StdOut;
    }

    public void setStdOut(String StdOut) {
        this.StdOut = StdOut;
    }

    public String getStdErr() {
        return this.StdErr;
    }

    public void setStdErr(String StdErr) {
        this.StdErr = StdErr;
    }

    public String getCorrelationId() {
        return this.CorrelationId;
    }

    public void setCorrelationId(String CorrelationId) {
        this.CorrelationId = CorrelationId;
    }

    public String getError() {
        return this.Error;
    }

    public void setError(String Error) {
        this.Error = Error;
    }
    
    public ResponseQueueMessage(String correlationId,String stdOut,String stdErr){
        setCorrelationId(correlationId);
        setStdOut(stdOut);
        setStdErr(stdErr);
        setError(null);
    } 

    public ResponseQueueMessage(String correlationId,String error){
        setError(error);
    }
}
