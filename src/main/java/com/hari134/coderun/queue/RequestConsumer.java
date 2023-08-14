package com.hari134.coderun.queue;

import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.hari134.coderun.api.dto.container.ContainerResponse;
import com.hari134.coderun.api.dto.queue.RequestQueueMessage;
import com.hari134.coderun.containers.ContainerPool;

@Component
public class RequestConsumer {
    private final ContainerPool containerPool;
    private RabbitTemplate rabbitTemplate;
    private ResponseProducer responseProducer;

    @Autowired
    public RequestConsumer(RabbitTemplate rabbitTemplate,ContainerPool containerPool,ResponseProducer responseProducer) {
        this.containerPool = containerPool;
        this.responseProducer = responseProducer;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async // Enable asynchronous execution
    @RabbitListener(queues = "request-queue")
    public void processRequest(Message requestMessage) {
        RequestQueueMessage queueMessage = (RequestQueueMessage) rabbitTemplate.getMessageConverter()
                .fromMessage(requestMessage);

        String language = queueMessage.getLanguage();
        String code = queueMessage.getCode();
        String correlationId = requestMessage.getMessageProperties().getCorrelationId();

        // Execute the code asynchronously using the container pool
        CompletableFuture<ContainerResponse> executionResultFuture;
        try {
            executionResultFuture = containerPool.executeAsync(language, code);
        } catch (Exception e) {
            // Handle the exception that occurred during executeAsync
            responseProducer.sendErrorToQueue(correlationId,e);
            return;
        }

        executionResultFuture.thenAccept(result -> {
            try {
                responseProducer.sendResponseToQueue(correlationId,result);
            } catch (Exception e) {
                responseProducer.sendErrorToQueue(correlationId,e);
            }
        }).exceptionally(exception -> {
            try {
                responseProducer.sendErrorToQueue(correlationId,exception);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

}
