package com.hari134.coderun.queue;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hari134.coderun.api.dto.container.ContainerResponse;
import com.hari134.coderun.api.dto.queue.ResponseQueueMessage;

@Component
public class ResponseProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ResponseProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendResponseToQueue(String correlationId, ContainerResponse result) {
        ResponseQueueMessage queueMessage = new ResponseQueueMessage(correlationId, result.getStdOut(), result.getStdErr());
        publishResponseToQueue(correlationId, queueMessage);
    }

    public void sendErrorToQueue(String correlationId, Throwable exception) {
        ResponseQueueMessage queueMessage = new ResponseQueueMessage(correlationId, exception.toString());
        publishResponseToQueue(correlationId, queueMessage);
    }

    private void publishResponseToQueue(String correlationId, ResponseQueueMessage queueMessage) {
        rabbitTemplate.convertAndSend("response-queue", queueMessage, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setCorrelationId(correlationId);
            return message;
        });
    }
}

