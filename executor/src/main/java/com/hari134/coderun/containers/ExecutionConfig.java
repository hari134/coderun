package com.hari134.coderun.containers;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import com.hari134.coderun.enums.Language;
import com.hari134.coderun.enums.Mode;

public class ExecutionConfig {
    // Constants and Variables
    private Language language;
    private ExecCmd execCmd;
    private String filePath;
    private String code;
    private Mode mode;


    private static final Map<Language, Value> LANGUAGE_CONFIG_MAP = new HashMap<>();

    static {
        // Populate the map with valid languages and their execution commands
        LANGUAGE_CONFIG_MAP.put(Language.CPP, new Value("cpp", new ExecCmd("./scripts/run/cpp.sh", "./scripts/judge/cpp.sh")));
        // LANGUAGE_CONFIG_MAP.put("python", new Value("python", new
        // ExecCmd("./scripts/run/python.sh","./scripts/judge/python.sh")));
    }

    public ExecutionConfig(Language language,String code, Mode mode) {
        validateLanguage(language);
        setLanguage(language);
        setFilePath(language);
        setCode(code);        
        setMode(mode);
        setExecCmd(language);
    }

    
    private void validateLanguage(Language language) {
        if (!LANGUAGE_CONFIG_MAP.containsKey(language)) {
            throw new IllegalArgumentException("Invalid language: " + language);
        }
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
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
        return execCmd.getCommand(mode);
    }

    private void setExecCmd(Language language) {
        ExecCmd execCmd = LANGUAGE_CONFIG_MAP.get(language).getExecCmd();
        this.execCmd = execCmd;
    }

    public String getFilePath() {
        return filePath;
    }

    private void setFilePath(Language language) {
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

//TODO ExecCmd will be either for judge or execute based on mode enum
class ExecCmd {
    private HashMap<Mode,String> commands;
    public ExecCmd(String execute, String judge) {
        commands.put(Mode.EXECUTE, execute);
        commands.put(Mode.JUDGE, judge);
    }

    public String getCommand(Mode mode) {
        return commands.get(mode);
    }
}