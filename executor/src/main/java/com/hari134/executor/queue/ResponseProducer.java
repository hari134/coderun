package com.hari134.executor.queue;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hari134.executor.dto.judge.ContainerResponse;
import com.hari134.executor.dto.queue.SubmissionResponseQueueMessage;

@Component
public class ResponseProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ResponseProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendResponseToQueue(String correlationId, ContainerResponse result) {
        SubmissionResponseQueueMessage queueMessage = SubmissionResponseQueueMessage.fromJson(result.getStdOut(),result.getStdErr(),correlationId);
        publishResponseToQueue(correlationId, queueMessage);
    }

    public void sendErrorToQueue(String correlationId, Throwable exception) {
        System.out.println("error queue");
        // SubmissionResponseQueueMessage queueMessage = SubmissionResponseQueueMessage(correlationId, exception.toString());
        // publishResponseToQueue(correlationId, queueMessage);
    }

    private void publishResponseToQueue(String correlationId, SubmissionResponseQueueMessage queueMessage) {
        rabbitTemplate.convertAndSend("response-queue", queueMessage, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setCorrelationId(correlationId);
            return message;
        });
    }
}

