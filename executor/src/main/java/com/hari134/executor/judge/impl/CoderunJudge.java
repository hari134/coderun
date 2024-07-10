package com.hari134.executor.judge.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.annotation.PreDestroy;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.hari134.executor.dto.judge.ContainerResponse;
import com.hari134.executor.dto.judge.ExecutionConfig;
import com.hari134.executor.judge.AbstractIsolateDockerContainer;
import com.hari134.executor.judge.JudgeContainer;

@Component
public class CoderunJudge extends AbstractIsolateDockerContainer implements JudgeContainer {

    private final DockerClient dockerClient;
    private final String containerId;

    @Autowired
    CoderunJudge(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
        this.containerId = startContainer();
    }

    private String startContainer() {
        try {
            // Create Docker container from the pre-built image
            CreateContainerResponse container = dockerClient.createContainerCmd("coderun-judge-container")
                    .withNetworkDisabled(true)
                    .withPrivileged(true)
                    .withName("judge-container-" + UUID.randomUUID().toString())
                    .exec();

            String containerId = container.getId();

            // Start the container
            dockerClient.startContainerCmd(containerId).exec();

            return containerId;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start container", e);
        }
    }

    public CompletableFuture<ContainerResponse> executeAsync(ExecutionConfig executionConfig) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                writeDataToFileInContainer(executionConfig.getCodeFilePath(), executionConfig.getCode());
                if (executionConfig.getStdin() != "") {
                    writeDataToFileInContainer(executionConfig.getStdinPath(), executionConfig.getStdin());
                }
                if (executionConfig.getExpectedOutput() != "") {
                    writeDataToFileInContainer(executionConfig.getExpectedOutputFilePath(),
                            executionConfig.getExpectedOutput());
                }
                // Create exec instance inside a running container
                ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                        .withAttachStdout(true)
                        .withAttachStderr(true)
                        .withCmd("/bin/sh", "-c", executionConfig.getCommand())
                        .exec();

                ByteArrayOutputStream stdout = new ByteArrayOutputStream();
                ByteArrayOutputStream stderr = new ByteArrayOutputStream();

                // Start the execution of previously created exec instance
                dockerClient.execStartCmd(execCreateCmdResponse.getId())
                        .exec(new ExecStartResultCallback(stdout, stderr))
                        .awaitCompletion();

                return new ContainerResponse(stdout.toString(), stderr.toString());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            } catch (Exception e) {
                throw new RuntimeException("Failed to execute in container", e);
            }
        });
    }

    public void writeDataToFileInContainer(String filePath, String data) {
        byte[] tarData = new byte[0];
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream)) {
            TarArchiveEntry entry = new TarArchiveEntry(filePath);
            entry.setSize(data.getBytes(StandardCharsets.UTF_8).length);
            entry.setMode(0644);

            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.finish();

            tarData = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream uploadStream = new ByteArrayInputStream(tarData)) {
            dockerClient.copyArchiveToContainerCmd(containerId)
                    .withTarInputStream(uploadStream)
                    .withRemotePath("/")
                    .exec();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void cleanupContainer() {
        try {
            dockerClient.stopContainerCmd(containerId).exec();

            dockerClient.removeContainerCmd(containerId).exec();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean up container", e);
        }
    }


}
