package com.hari134.executor.judge;

import java.util.concurrent.CompletableFuture;

import com.hari134.executor.dto.judge.ContainerResponse;
import com.hari134.executor.dto.judge.ExecutionConfig;

public interface JudgeContainer {
    public CompletableFuture<ContainerResponse> executeAsync(ExecutionConfig executionConfig) throws InterruptedException;
    public void writeDataToFileInContainer(String filePath,String data);
}