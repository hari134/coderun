package com.hari134.coderun.queue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.hari134.coderun.dto.judge.ContainerResponse;
import com.hari134.coderun.dto.judge.ExecutionConfig;
import com.hari134.coderun.dto.queue.RequestQueueMessage;
import com.hari134.coderun.judge.impl.CoderunJudge;

@Component
public class RequestConsumer {
    private final CoderunJudge coderunJudge;
    private RabbitTemplate rabbitTemplate;
    private ResponseProducer responseProducer;
    private final Semaphore semaphore;

    @Autowired
    public RequestConsumer(RabbitTemplate rabbitTemplate, CoderunJudge coderunJudge,
            ResponseProducer responseProducer) {
        this.coderunJudge = coderunJudge;
        this.responseProducer = responseProducer;
        this.rabbitTemplate = rabbitTemplate;
        this.semaphore = new Semaphore(10);
    }

    @RabbitListener(queues = "request-queue")
    public void processRequest(Message requestMessage) {
        if (!acquireSemaphore()) {
            return;
        }

        try {
            RequestQueueMessage queueMessage = convertMessage(requestMessage);
            if (queueMessage == null) {
                semaphore.release();
                return;
            }

            ExecutionConfig executionConfig = buildExecutionConfig(requestMessage, queueMessage);
            CompletableFuture<ContainerResponse> executionResultFuture = executeConfigAsync(executionConfig);

            handleExecutionResultAsync(executionResultFuture, executionConfig.getCorrelationId());
        } catch (Exception e) {
            responseProducer.sendErrorToQueue(getCorrelationId(requestMessage), e);
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

    private RequestQueueMessage convertMessage(Message requestMessage) {
        return (RequestQueueMessage) rabbitTemplate.getMessageConverter().fromMessage(requestMessage);
    }

    private ExecutionConfig buildExecutionConfig(Message requestMessage, RequestQueueMessage queueMessage) {
        String correlationId = requestMessage.getMessageProperties().getCorrelationId();
        String boxId = coderunJudge.getUniqueBoxId();
        return new ExecutionConfig(
                correlationId,
                queueMessage.getLanguage(),
                queueMessage.getCode(),
                queueMessage.getPathToCppFile(),
                queueMessage.getTimeLimit(),
                queueMessage.getMemoryLimit(),
                boxId,
                queueMessage.getStdinPath(),
                queueMessage.getExpectedOutputFile());
    }

    private CompletableFuture<ContainerResponse> executeConfigAsync(ExecutionConfig executionConfig) {
        return coderunJudge.executeAsync(executionConfig);
    }

    private void handleExecutionResultAsync(CompletableFuture<ContainerResponse> future, String correlationId) {
        future.thenAccept(result -> {
            try {
                responseProducer.sendResponseToQueue(correlationId, result);
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

    private String getCorrelationId(Message message) {
        return message.getMessageProperties().getCorrelationId();
    }
}
