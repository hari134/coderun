package com.hari134.coderun.containers;

import java.util.Map;

import static java.util.Map.entry;

public class LanguageConfig {
    // Constructor
    public LanguageConfig(String language){
        setLanguage(language);
        setImage(LANGUAGE_IMAGES.get(language));
        setExecCmd(LANGUAGE_CMD.get(language));
        setContainerPath(LANGUAGE_CONTAINER_PATHS.get(language));
    }

    // Constants and Variables

    private String language;
    private String image;
    private String[] execCmd;
    private String containerPath;
    private static final Map<String,String> LANGUAGE_IMAGES = Map.ofEntries(
            entry("python","python:latest"),
            entry("java","openjdk:11-jre-slim"), /* fix java */
            entry("cpp","gcc:latest"),
            entry("c","gcc:latest")
    );
    private static final Map<String,String[]> LANGUAGE_CMD = Map.ofEntries(
            entry("python", new String[]{"timeout","2","python", "main.py"}),
            entry("java",new String[]{"timeout","2","java" ,"main.java"}), /* fix java */
            entry("cpp", new String[]{"sh", "-c", "g++ -o app main.cpp && ./app"}),
            entry("c", new String[]{"sh", "-c", "gcc -o app main.c && ./app"})
    );

    private static final Map<String,String> LANGUAGE_CONTAINER_PATHS = Map.ofEntries(
            entry("python","main.py"),
            entry("java","main.java"),
            entry("cpp","main.cpp"),
            entry("c","main.c")
    );



    // Getters and setters
    public String[] getExecCmd() {
        return execCmd;
    }

    public void setExecCmd(String[] execCmd) {
        this.execCmd = execCmd;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContainerPath() {
        return containerPath;
    }

    public void setContainerPath(String containerPath) {
        this.containerPath = containerPath;
    }


}
