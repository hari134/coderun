package com.hari134.executor.dto.judge;

public class ContainerResponse {
    private String StdOut;
    private String StdErr;

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

    public ContainerResponse(String StdOut,String StdErr){
        this.StdOut = StdOut;
        if(StdErr.contains("OK")){
            this.StdErr = "";
        }
        else{
            this.StdErr = StdErr;
        }
    }
}
