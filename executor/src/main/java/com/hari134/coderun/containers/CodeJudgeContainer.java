package com.hari134.coderun.containers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.hari134.coderun.dto.container.ContainerResponse;

@Component
public class CodeJudgeContainer implements ExecutionContainer {

    private final DockerClient dockerClient;
    private final String containerId;

    @Autowired
    CodeJudgeContainer(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
        this.containerId = startContainer();
    }

    private String startContainer() {
        // Build Docker image from Dockerfile
        String imageId = dockerClient.buildImageCmd()
                .withDockerfile(new File(getClass().getResource("/Dockerfile").getFile()))
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        // Create Docker container from the built image
        CreateContainerResponse container = dockerClient.createContainerCmd(imageId)
                .withName("judge-container")
                .exec();

        String containerId = container.getId();

        // Start the container
        dockerClient.startContainerCmd(containerId).exec();

        return containerId;
    }

    public ContainerResponse execute(ExecutionConfig executionConfig) throws InterruptedException {
        writeCodeToFileInContainer(executionConfig.getFilePath(), executionConfig.getCode());
        // Create exec instance inside a running container
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(executionConfig.getCommand())
                .exec();

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        // Start the execution of previously created exec instance
        dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .exec(new ExecStartResultCallback(stdout, stderr))
                .awaitCompletion();

        return new ContainerResponse(stdout.toString(), stderr.toString());
    }

    public void writeCodeToFileInContainer(String filePath, String code) {
        byte[] tarData = new byte[0];
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream)) {
            TarArchiveEntry entry = new TarArchiveEntry(filePath);
            entry.setSize(code.getBytes(StandardCharsets.UTF_8).length);
            entry.setMode(0644); // Same as 0644 in Go

            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write(code.getBytes(StandardCharsets.UTF_8));
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.finish();

            tarData = byteArrayOutputStream.toByteArray();
            // At this point, 'tarData' contains the tar data.

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream uploadStream = new ByteArrayInputStream(tarData)) {
            dockerClient
                    .copyArchiveToContainerCmd(containerId)
                    .withTarInputStream(uploadStream)
                    .withRemotePath("/") // Change the remote path to where you want the code to be copied in the
                                         // container
                    .exec();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
