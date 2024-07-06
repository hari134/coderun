package com.hari134.api_gateway.dto.judge;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

class LanguageExtensionsMappings {
  static final HashMap<String, String> EXTENSIONS;

  static {
    EXTENSIONS = new HashMap<>();
    EXTENSIONS.put("cpp", "cpp");
  }
}
public class ExecutionConfig {
    // Constants and Variables
    private String language;
    private String execCmd;
    private String filePath;
    private String code;


    public ExecutionConfig(String language,String code, Mode mode) {
        // validateLanguage(language); TODO
        setLanguage(language);
        setFilePathFromLanguage(language);
        setCode(code);
        setExecCmd(language);
    }


    // private void validateLanguage(Language language) {
    //     if (!LANGUAGE_CONFIG_MAP.containsKey(language)) {
    //         throw new IllegalArgumentException("Invalid language: " + language);
    //     }
    // }

    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    private void setLanguage(String language) {
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

    private void setFilePathFromLanguage(String language) {
        String filePath = UUID.randomUUID().toString() + LANGUAGE_CONFIG_MAP.get(language).getFileExtension();
        this.filePath = filePath;
    }

}

