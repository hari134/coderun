package com.hari134.coderun.containers;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ContainerUtil {

    public static void startContainer(DockerClient dockerClient,String containerId) {
        // Start the container
        dockerClient.startContainerCmd(containerId).exec();
    }
    public static void writeCodeToContainer(DockerClient dockerClient, LanguageConfig languageConfig, String containerId,String code) {
        byte[] tarData = new byte[0];
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream)) {

            TarArchiveEntry entry = new TarArchiveEntry(languageConfig.getContainerPath());
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
                    .withRemotePath("/") // Change the remote path to where you want the code to be copied in the container
                    .exec();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeContainer(DockerClient dockerClient, String containerId) {
        try {
            // Kill the container
            dockerClient.killContainerCmd(containerId).exec();
        } catch (NotFoundException e) {
            System.err.println("Container or network not found. Nothing to remove.");
        } catch (NotModifiedException e) {
            System.err.println("Container or network has not changed. Nothing to remove.");
        } catch (Exception e) {
            System.err.println("Failed to remove container or network.");
            e.printStackTrace();
        }
    }
}
