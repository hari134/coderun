package com.hari134.api_gateway.service.impl.queue;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.hari134.api_gateway.api.util.DeferredResultManager;
import com.hari134.api_gateway.dto.api.SubmissionResponseWithWait;
import com.hari134.api_gateway.dto.queue.SubmissionRequestQueueMessage;
import com.hari134.api_gateway.service.QueueService;

@Service
public class RabbitMQServiceImpl implements QueueService {
    private final RabbitTemplate rabbitTemplate;
    private final DeferredResultManager deferredResultManager;

    @Autowired
    public RabbitMQServiceImpl(RabbitTemplate rabbitTemplate, DeferredResultManager deferredResultManager) {
        this.rabbitTemplate = rabbitTemplate;
        this.deferredResultManager = deferredResultManager;
    }

    @Override
    public void sendRequest(SubmissionRequestQueueMessage queueMessage) {
        // Publish the request to the request queue with the correlation ID
        String correlationId = queueMessage.getCorrelationId();
        rabbitTemplate.convertAndSend("request-queue", queueMessage, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            return message;
        });
    }

    @RabbitListener(queues = "response-queue")
    @Override
    public void processResponse(String correlationId, SubmissionResponseWithWait userResponse) {
        DeferredResult<SubmissionResponseWithWait> deferredResult = deferredResultManager.removeDeferredResult(correlationId);
        if (deferredResult != null) {
            // Set the response based on the message received
            deferredResult.setResult(userResponse);
        }
    }
}

