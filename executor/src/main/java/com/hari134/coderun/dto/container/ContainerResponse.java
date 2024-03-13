package com.hari134.coderun.dto.container;

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
        this.StdErr = StdErr;
    }
}
