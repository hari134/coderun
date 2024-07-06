package com.hari134.coderun.dto.queue;

import java.io.Serializable;

public class RequestQueueMessage implements Serializable{
    private String language;
    private String code;
    private String pathToCppFile;
    private String timeLimit;
    private String memoryLimit;
    private String stdinPath;
    private String expectedOutputFile;


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPathToCppFile() {
        return pathToCppFile;
    }

    public void setPathToCppFile(String pathToCppFile) {
        this.pathToCppFile = pathToCppFile;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(String memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public String getStdinPath() {
        return stdinPath;
    }

    public void setStdinPath(String stdinPath) {
        this.stdinPath = stdinPath;
    }

    public String getExpectedOutputFile() {
        return expectedOutputFile;
    }

    public void setExpectedOutputFile(String expectedOutputFile) {
        this.expectedOutputFile = expectedOutputFile;
    }
}

