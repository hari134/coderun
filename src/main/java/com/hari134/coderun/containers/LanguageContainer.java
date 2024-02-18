package com.hari134.coderun.containers;

import java.io.ByteArrayOutputStream;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.hari134.coderun.dto.container.ContainerResponse;

public class LanguageContainer {
    public String ContainerId;
    private LanguageConfig languageConfig;

    // Output
    private String StdOut;
    private String StdErr;

    // Constructor
    public LanguageContainer(DockerClient dockerClient, LanguageConfig languageConfig) {
        setLanguageConfig(languageConfig);
        setupHostConfig(dockerClient, languageConfig.getImage());
        ContainerUtil.startContainer(dockerClient,getContainerId());
    }

    // Helper functions
    private void setupHostConfig(DockerClient dockerClient, String imageName) {
        // setup host config
        HostConfig hostConfig = new HostConfig()
                .withNetworkMode("none");

        // Create a container
        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(imageName)
                .withTty(true)
                .withHostConfig(hostConfig)
                .exec();

        setContainerId(containerResponse.getId());
    }

    //Execute Code
    public ContainerResponse Execute(DockerClient dockerClient, String code) throws InterruptedException {
        // Write code to container
        ContainerUtil.writeCodeToContainer(dockerClient, getLanguageConfig(),getContainerId(),code);
        // Create exec instance inside a running container
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(getContainerId())
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(getLanguageConfig().getExecCmd())
                .exec();

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        // Start the execution of previously created exec instance
        dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .exec(new ExecStartResultCallback(stdout, stderr))
                .awaitCompletion();

        return new ContainerResponse(stdout.toString(),stderr.toString());
    }

    // Getters and Setters

    public void setLanguageConfig(LanguageConfig languageConfig) {
        this.languageConfig = languageConfig;
    }

    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    public void setStdOut(String stdOut) {
        StdOut = stdOut;
    }

    public void setStdErr(String stdErr) {
        StdErr = stdErr;
    }

    public String getStdOut() {
        String _StdOut = StdOut;
        setStdOut("");
        return _StdOut;
    }

    public String getStdErr() {
        String _StdErr = StdErr;
        setStdErr("");
        return _StdErr;
    }

    public void setContainerId(String containerId) {

        this.ContainerId = containerId;
    }

    public String getContainerId() {
        return ContainerId;
    }


    // Constants
    private static final Long MEMORY_LIMIT = 64 * 1024 * 1024L; // 64 MB RAM

}
