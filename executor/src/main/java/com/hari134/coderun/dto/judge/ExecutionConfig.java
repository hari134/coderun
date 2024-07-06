package com.hari134.coderun.dto.judge;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import com.hari134.coderun.enums.Language;

public class ExecutionConfig {
    // Constants and Variables
    private String correlationId;
    private Language language;
    private ExecCmd execCmd;
    private String filePath;
    private String code;
    private boolean wait;


    private static final Map<Language, String> LANGUAGE_FILE_EXTENSION_MAP = new HashMap<>();
    private static final Map<Language, String> LANGUAGE_EXEC_CMD_MAP = new HashMap<>();

    static {
        // Populate the maps with valid languages and their execution commands and file extensions
        LANGUAGE_FILE_EXTENSION_MAP.put(Language.CPP, "cpp");
        LANGUAGE_EXEC_CMD_MAP.put(Language.CPP, "./scripts/run/cpp.sh");
    }

    public ExecutionConfig(String correlationId,String language, String code, String pathToCppFile, String timeLimit, String memoryLimit, String boxId, String stdinPath, String expectedOutputFile) {
        Language langEnum = convertToLanguageEnum(language);
        validateLanguage(langEnum);
        setLanguage(langEnum);
        setFilePathFromLanguage(langEnum);
        setCode(code);
        setCorrelationId(correlationId);
        setExecCmd(langEnum, pathToCppFile, timeLimit, memoryLimit, boxId, stdinPath, expectedOutputFile);
    }

    private Language convertToLanguageEnum(String language) {
        try {
            return Language.valueOf(language.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid language: " + language, e);
        }
    }

    private void validateLanguage(Language language) {
        if (!LANGUAGE_FILE_EXTENSION_MAP.containsKey(language) || !LANGUAGE_EXEC_CMD_MAP.containsKey(language)) {
            throw new IllegalArgumentException("Invalid language: " + language);
        }
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Language getLanguage() {
        return language;
    }

    private void setLanguage(Language language) {
        this.language = language;
    }

    public String getCommand() {
        return execCmd.getCommand();
    }

    private void setExecCmd(Language language, String pathToCppFile, String timeLimit, String memoryLimit, String boxId, String stdinPath, String expectedOutputFile) {
        String commandTemplate = LANGUAGE_EXEC_CMD_MAP.get(language);
        ExecCmd execCmd = new ExecCmd(commandTemplate, pathToCppFile, timeLimit, memoryLimit, boxId, stdinPath, expectedOutputFile);
        this.execCmd = execCmd;
    }

    public String getFilePath() {
        return filePath;
    }

    private void setFilePathFromLanguage(Language language) {
        String filePath = UUID.randomUUID().toString() + "." + LANGUAGE_FILE_EXTENSION_MAP.get(language);
        this.filePath = filePath;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }
}

class ExecCmd {
    private String command;

    public ExecCmd(String commandTemplate, String pathToCppFile, String timeLimit, String memoryLimit, String boxId, String stdinPath, String expectedOutputFile) {
        this.command = buildCommand(commandTemplate, pathToCppFile, timeLimit, memoryLimit, boxId, stdinPath, expectedOutputFile);
    }

    private String buildCommand(String commandTemplate, String pathToCppFile, String timeLimit, String memoryLimit, String boxId, String stdinPath, String expectedOutputFile) {
        StringBuilder command = new StringBuilder(commandTemplate);
        command.append(" ").append(pathToCppFile);
        command.append(" ").append(timeLimit);
        command.append(" ").append(memoryLimit);
        command.append(" ").append(boxId);
        if (stdinPath != null && !stdinPath.isEmpty()) {
            command.append(" ").append(stdinPath);
        }
        if (expectedOutputFile != null && !expectedOutputFile.isEmpty()) {
            command.append(" ").append(expectedOutputFile);
        }
        return command.toString();
    }

    public String getCommand() {
        return command;
    }
}
