package com.hari134.executor.queue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hari134.executor.dto.judge.ContainerResponse;
import com.hari134.executor.dto.judge.ExecutionConfig;
import com.hari134.executor.dto.queue.SubmissionRequestQueueMessage;
import com.hari134.executor.dto.queue.SubmissionResponseQueueMessage;
import com.hari134.executor.judge.impl.CoderunJudge;
import com.hari134.executor.service.CodeSubmissionService;

@Component
public class RequestConsumer {
    private final CoderunJudge coderunJudge;
    private final ResponseProducer responseProducer;
    private final Semaphore semaphore;
    private final CodeSubmissionService codeSubmissionService;

    @Autowired
    public RequestConsumer(CoderunJudge coderunJudge, ResponseProducer responseProducer, CodeSubmissionService codeSubmissionService) {
        this.coderunJudge = coderunJudge;
        this.responseProducer = responseProducer;
        this.semaphore = new Semaphore(10);
        this.codeSubmissionService = codeSubmissionService;
    }

    @RabbitListener(queues = "request-queue")
    public void processRequest(SubmissionRequestQueueMessage queueMessage) {
        if (!acquireSemaphore()) {
            return;
        }

        try {
            if (queueMessage == null) {
                semaphore.release();
                return;
            }

            ExecutionConfig executionConfig = buildExecutionConfig(queueMessage.getCorrelationId(), queueMessage);
            CompletableFuture<ContainerResponse> executionResultFuture = coderunJudge.executeAsync(executionConfig);

            if (queueMessage.getWait()) {
                // Handle synchronous response
                ContainerResponse result = executionResultFuture.join();
                handleExecutionResultSync(result, executionConfig.getCorrelationId());
            } else {
                // Handle asynchronous response
                handleExecutionResultAsync(executionResultFuture, executionConfig.getCorrelationId());
            }
        } catch (Exception e) {
            responseProducer.sendErrorToQueue(queueMessage.getCorrelationId(), e);
            semaphore.release();
        }
    }

    private boolean acquireSemaphore() {
        try {
            semaphore.acquire();
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private ExecutionConfig buildExecutionConfig(String correlationId, SubmissionRequestQueueMessage queueMessage) {
        String boxId = coderunJudge.getUniqueBoxId();
        return new ExecutionConfig(
                correlationId,
                queueMessage.getLanguage(),
                queueMessage.getCode(),
                queueMessage.getTimeLimit(),
                queueMessage.getWallTimeLimit(),
                queueMessage.getMemoryLimit(),
                boxId,
                queueMessage.getStdin(),
                queueMessage.getExpectedOutput());
    }

    private void handleExecutionResultSync(ContainerResponse result, String correlationId) {
        try {
            responseProducer.sendResponseToQueue(correlationId, result);
        } catch (Exception e) {
            System.out.println(e);
            responseProducer.sendErrorToQueue(correlationId, e);
        } finally {
            semaphore.release();
        }
    }

    private void handleExecutionResultAsync(CompletableFuture<ContainerResponse> future, String correlationId) {
        future.thenAccept(result -> {
            try {
                saveAsyncSubmissionResult(correlationId, result);
            } catch (Exception e) {
                responseProducer.sendErrorToQueue(correlationId, e);
            } finally {
                semaphore.release();
            }
        }).exceptionally(exception -> {
            try {
                responseProducer.sendErrorToQueue(correlationId, exception);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
            return null;
        });
    }

    private void saveAsyncSubmissionResult(String correlationId, ContainerResponse result) {
        SubmissionResponseQueueMessage queueMessage = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),result.getStdErr(),correlationId);
        codeSubmissionService.saveOnAsyncRequestCompletion(correlationId, queueMessage);
    }
}
