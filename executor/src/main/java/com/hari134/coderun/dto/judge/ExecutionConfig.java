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

    private String codeFilePath;
    private String code;

    private String expectedOutput;
    private String expectedOutputFilePath;

    private String stdin;
    private String stdinPath;

    private boolean wait;


    private static final Map<Language, String> LANGUAGE_FILE_EXTENSION_MAP = new HashMap<>();
    private static final Map<Language, String> LANGUAGE_EXEC_CMD_MAP = new HashMap<>();

    static {
        // Populate the maps with valid languages and their execution commands and file extensions
        LANGUAGE_FILE_EXTENSION_MAP.put(Language.CPP, "cpp");
        LANGUAGE_EXEC_CMD_MAP.put(Language.CPP, "scripts/run/cpp.sh");
    }

    public ExecutionConfig(String correlationId,String language, String code, String timeLimit, String memoryLimit, String boxId, String stdin, String expectedOutput) {
        Language langEnum = convertToLanguageEnum(language);
        validateLanguage(langEnum);
        setLanguage(langEnum);
        setCodeFilePathFromLanguage(langEnum);
        setCode(code);
        setStdin(stdin);
        setExpectedOutput(expectedOutput);
        setCorrelationId(correlationId);
        String codeFilePath = getCodeFilePath();
        String expectedOutputFilePath = "";
        String stdinFilePath = "";
        if(stdin != ""){
            setStdinFilePath();
            stdinFilePath = getStdinPath();
        }
        if(expectedOutput != ""){
            setExpectedOutput(expectedOutputFilePath);
            expectedOutputFilePath = getExpectedOutputFilePath();
        }

        setExecCmd(langEnum, codeFilePath, timeLimit, memoryLimit, boxId, stdinFilePath, expectedOutput);
    }

    public String getStdinPath() {
        return stdinPath;
    }

    public void setStdinPath(String stdinPath) {
        this.stdinPath = stdinPath;
    }

    public String getStdin() {
        return stdin;
    }

    public void setStdin(String stdin) {
        this.stdin = stdin;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public String getExpectedOutputFilePath() {
        return expectedOutputFilePath;
    }

    public void setExpectedOutputFilePath(String expectedOutputFilePath) {
        this.expectedOutputFilePath = expectedOutputFilePath;
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

    private void setExecCmd(Language language, String codeFilePath, String timeLimit, String memoryLimit, String boxId, String stdinFilePath, String expectedOutputFilePath) {
        String commandTemplate = LANGUAGE_EXEC_CMD_MAP.get(language);
        ExecCmd execCmd = new ExecCmd(commandTemplate, codeFilePath, timeLimit, memoryLimit, boxId, stdinFilePath, expectedOutputFilePath);
        this.execCmd = execCmd;
    }

    public String getCodeFilePath() {
        return codeFilePath;
    }

    private void setCodeFilePathFromLanguage(Language language) {
        String codeFilePath = UUID.randomUUID().toString() + "." + LANGUAGE_FILE_EXTENSION_MAP.get(language);
        this.codeFilePath = codeFilePath;
    }

    private void setStdinFilePath() {
        String stdinFilePath = UUID.randomUUID().toString() + ".txt";
        this.stdinPath = stdinFilePath;
    }

    private void setExpectedOutputFilePath() {
        String expectedOutputFilePath = UUID.randomUUID().toString() + ".txt";
        this.expectedOutputFilePath = expectedOutputFilePath;
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

    public ExecCmd(String commandTemplate, String pathToCodeFile, String timeLimit, String memoryLimit, String boxId, String stdinPath, String expectedOutputFile) {
        this.command = buildCommand(commandTemplate, pathToCodeFile, timeLimit, memoryLimit, boxId, stdinPath, expectedOutputFile);
    }

    private String buildCommand(String commandTemplate, String pathToCodeFile, String timeLimit, String memoryLimit, String boxId, String stdinPath, String expectedOutputFile) {
        StringBuilder command = new StringBuilder(commandTemplate);
        command.append(" ").append(pathToCodeFile);
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
