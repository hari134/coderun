package com.hari134.coderun.containers;

import com.github.dockerjava.api.DockerClient;

public class ContainerFactory {
    public static LanguageContainer createNewLanguageContainer(DockerClient dockerClient,String language){
        LanguageConfig languageConfig = new LanguageConfig("python");
        return new LanguageContainer(dockerClient,languageConfig);
    }
}
