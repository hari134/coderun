package com.hari134.coderun.containers;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.hari134.coderun.api.dto.container.ContainerResponse;

@Component
public class ContainerExecutor{
  private DockerClient dockerClient;

  @Autowired
  public ContainerExecutor(DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

  @Async
  public CompletableFuture<ContainerResponse> executeAsync(String language, String code) throws InterruptedException {
    LanguageContainer container = ContainerFactory.createNewLanguageContainer(dockerClient, language);
    try {
      // Execute the code using the obtained container
      ContainerResponse result = container.Execute(this.dockerClient, code);
      ContainerUtil.removeContainer(dockerClient, container.getContainerId());
      return CompletableFuture.completedFuture(result);
    } catch (Exception e) {
      // Handle exceptions and return an exceptionally completed CompletableFuture
      ContainerUtil.removeContainer(dockerClient, container.getContainerId());
      return CompletableFuture.failedFuture(e);
    }
  }
}
