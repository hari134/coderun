package com.hari134.executor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;

@Configuration
public class DockerClientConfig {

    @Bean
    public DockerClient dockerClient() {
        // Create and configure your DockerClient instance
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        // You can configure the DockerClient further if needed

        return dockerClient;
    }
}
