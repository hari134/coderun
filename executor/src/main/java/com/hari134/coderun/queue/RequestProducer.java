package com.hari134.coderun.queue;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hari134.coderun.api.dto.api.UserRequest;
import com.hari134.coderun.api.dto.queue.RequestQueueMessage;

@Component
public class RequestProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RequestProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void SendRequest(UserRequest userRequest, String correlationId) {
        String code = userRequest.getCode();
        String language = userRequest.getLanguage();
        RequestQueueMessage queueMessage = new RequestQueueMessage(correlationId,language, code);

        // Publish the request to the request queue with the correlation ID
        rabbitTemplate.convertAndSend("request-queue", queueMessage, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setCorrelationId(correlationId);
            return message;
        });
    }
}
