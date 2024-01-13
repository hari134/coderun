package com.hari134.coderun.containers;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.hari134.coderun.api.dto.container.ContainerResponse;


@Component
public class ContainerPool {
    private static final int POOL_SIZE = 2; // Set the number of language containers in each pool
    private final Map<String, BlockingQueue<LanguageContainer>> languagePools;
    private DockerClient dockerClient;

    @Autowired
    public ContainerPool(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
        this.languagePools = new ConcurrentHashMap<>();
    }

    private void initializePoolForLanguage(String language) {
        BlockingQueue<LanguageContainer> languagePool = new ArrayBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            languagePool.add(createNewLanguageContainer(language));
        }
        languagePools.put(language, languagePool);
    }

    private LanguageContainer createNewLanguageContainer(String language) {
        try {
            LanguageContainer container = ContainerFactory.createNewLanguageContainer(dockerClient, language);
            if (container == null) {
                // Handle the case where ContainerFactory failed to create a valid container
                throw new RuntimeException("Failed to create a new LanguageContainer for language: " + language);
            }
            return container;
        } catch (Exception e) {
            // Handle any exception that might occur during container creation
            throw new RuntimeException("Error creating LanguageContainer for language: " + language, e);
        }
    }
    

    private LanguageContainer take(String language) throws InterruptedException {
        BlockingQueue<LanguageContainer> languagePool = languagePools.get(language);
        if (languagePool == null) {
            initializePoolForLanguage(language);
            languagePool = languagePools.get(language);
        }
        return languagePool.take();
    }

    private void release(LanguageContainer languageContainer) {
        BlockingQueue<LanguageContainer> languagePool = languagePools.get(languageContainer.getLanguageConfig().getLanguage());
        if (languagePool != null) {
            languagePool.add(languageContainer);
        }
    }

    @Async
    public CompletableFuture<ContainerResponse> executeAsync(String language, String code) throws InterruptedException {
        LanguageContainer container = take(language);
        System.out.println("lang : "+ language+" code : "+code);
        try {
            // Execute the code using the obtained container
            ContainerResponse result = container.Execute(this.dockerClient,code);
            release(container);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            // Handle exceptions and return an exceptionally completed CompletableFuture
            release(container);
            return CompletableFuture.failedFuture(e);
        }
    }
}

