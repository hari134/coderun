package com.hari134.coderun.queue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.hari134.coderun.containers.ContainerExecutor;
import com.hari134.coderun.dto.container.ContainerResponse;
import com.hari134.coderun.dto.queue.RequestQueueMessage;

@Component
public class RequestConsumer {
    private final ContainerExecutor containerExecutor;
    private RabbitTemplate rabbitTemplate;
    private ResponseProducer responseProducer;
    private final Semaphore semaphore;

    @Autowired
    public RequestConsumer(RabbitTemplate rabbitTemplate, ContainerExecutor containerExecutor,
            ResponseProducer responseProducer) {
        this.containerExecutor = containerExecutor;
        this.responseProducer = responseProducer;
        this.rabbitTemplate = rabbitTemplate;
        this.semaphore = new Semaphore(10);
    }

    @RabbitListener(queues = "request-queue")
    public void processRequest(Message requestMessage) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        RequestQueueMessage queueMessage = (RequestQueueMessage) rabbitTemplate.getMessageConverter().fromMessage(requestMessage);

        String language = queueMessage.getLanguage();
        String code = queueMessage.getCode();
        String correlationId = requestMessage.getMessageProperties().getCorrelationId();

        CompletableFuture<ContainerResponse> executionResultFuture = null;
        try {
            executionResultFuture = containerExecutor.executeAsync(language, code);
        } catch (Exception e) {
            responseProducer.sendErrorToQueue(correlationId, e);
            semaphore.release();
        }

        if (executionResultFuture != null) {
            executionResultFuture.thenAccept(result -> {
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
    }
}
