package com.hari134.coderun.dto.container;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class ContainerResponse {
    private String error;
    private String output;
    private Metadata metadata;

    public ContainerResponse() {
    }

    // Method to deserialize JSON string to ContainerResponse object
    public static ContainerResponse fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, ContainerResponse.class);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}

class Metadata {
    private double time;
    private double timeWall;
    private int maxRss;
    private int cswVoluntary;
    private int cswForced;
    private int exitcode;
    public double getTime() {
      return time;
    }
    public void setTime(double time) {
      this.time = time;
    }
    public double getTimeWall() {
      return timeWall;
    }
    public void setTimeWall(double timeWall) {
      this.timeWall = timeWall;
    }
    public int getMaxRss() {
      return maxRss;
    }
    public void setMaxRss(int maxRss) {
      this.maxRss = maxRss;
    }
    public int getCswVoluntary() {
      return cswVoluntary;
    }
    public void setCswVoluntary(int cswVoluntary) {
      this.cswVoluntary = cswVoluntary;
    }
    public int getCswForced() {
      return cswForced;
    }
    public void setCswForced(int cswForced) {
      this.cswForced = cswForced;
    }
    public int getExitcode() {
      return exitcode;
    }
    public void setExitcode(int exitcode) {
      this.exitcode = exitcode;
    }

}
