package com.hari134.executor.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;

@Configuration
public class DockerClientConfig implements InitializingBean {

    @Value("${docker.host}")
    private String dockerHost;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dockerHost == null || dockerHost.isEmpty()) {
            throw new IllegalArgumentException("docker.host property must be set in application.properties");
        }
    }

    @Bean
    public DockerClient dockerClient() {
        return DockerClientBuilder.getInstance(dockerHost).build();
    }
}
