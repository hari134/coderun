package com.hari134.coderun.containers;

import java.util.Map;
import com.github.dockerjava.api.command.AttachContainerCmd.Exec;
import java.util.HashMap;
import java.util.UUID;

public class ExecutionConfig {
    // Constants and Variables
    private String language;
    private String execCmd;
    private String filePath;
    private static final Map<String, Value> LANGUAGE_CONFIG_MAP = new HashMap<>();

    static {
        // Populate the map with valid languages and their execution commands
        LANGUAGE_CONFIG_MAP.put("cpp", new Value("cpp", new ExecCmd("./scripts/run/cpp.sh", "./scripts/judge/cpp.sh")));
        // LANGUAGE_CONFIG_MAP.put("python", new Value("python", new
        // ExecCmd("./scripts/run/python.sh","./scripts/judge/python.sh")));
    }

    // Constructor
    public ExecutionConfig(String language,String type) {
        validateLanguage(language);
        setLanguage(language);
        setFilePath(language);
    }

    
    private void validateLanguage(String language) {
        if (!LANGUAGE_CONFIG_MAP.containsKey(language)) {
            throw new IllegalArgumentException("Invalid language: " + language);
        }
    }
    public String getLanguage() {
        return language;
    }

    private void setLanguage(String language) {
        this.language = language;
    }

    public String getExecCmd() {
        return execCmd;
    }

    private void setExecCmd(String execCmd) {
        String 
        this.execCmd = execCmd;
    }

    public String getFilePath() {
        return filePath;
    }

    private void setFilePath(String language) {
        String filePath = UUID.randomUUID().toString() + LANGUAGE_CONFIG_MAP.get(language).getFileExtension();
        this.filePath = filePath;
    }

}

class Value {
    private String fileExtension;
    private ExecCmd execCmd;

    public Value(String fileExtension, ExecCmd execCmd) {
        this.fileExtension = fileExtension;
        this.execCmd = execCmd;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public ExecCmd getExecCmd() {
        return execCmd;
    }
}

class ExecCmd {
    private String execute;
    private String judge;

    public ExecCmd(String execute, String judge) {
        this.judge = judge;
        this.execute = execute;
    }

    public String getJudge() {
        return judge;
    }

    public String getExecute() {
        return execute;
    }
}