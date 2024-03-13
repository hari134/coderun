package com.hari134.coderun.containers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import com.github.dockerjava.api.DockerClient;
import com.hari134.coderun.containers.ExecutionContainer;
import com.hari134.coderun.dto.container.ContainerResponse;
import java.util.UUID;

public class CodeJudgeContainer implements ExecutionContainer {

    public ContainerResponse execute(ExecutionConfig executionConfig) {
        return new ContainerResponse("", "");
    }

    public void writeCodeToContainer(DockerClient dockerClient, ExecutionConfig executionConfig, String containerId,
            String code) {
        byte[] tarData = new byte[0];
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream)) {
            String filePath = executionConfig. 
            TarArchiveEntry entry = new TarArchiveEntry();
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
