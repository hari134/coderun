package com.hari134.coderun.dto.api;

public class UserResponse {
    private String StdOut;
    private String StdErr;
    private String Error;

    public String getError() {
        return this.Error;
    }

    public void setError(String Error) {
        this.Error = Error;
    }

    public String getStdOut() {
        return StdOut;
    }

    public void setStdOut(String stdOut) {
        StdOut = stdOut;
    }

    public String getStdErr() {
        return StdErr;
    }

    public void setStdErr(String stdErr) {
        StdErr = stdErr;
    }
}
