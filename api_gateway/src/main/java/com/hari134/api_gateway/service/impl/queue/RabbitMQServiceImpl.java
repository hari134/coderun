package com.hari134.api_gateway.service.impl.queue;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.hari134.api_gateway.api.util.DeferredResultManager;
import com.hari134.api_gateway.dto.queue.SubmissionRequestQueueMessage;
import com.hari134.api_gateway.dto.queue.SubmissionResponseQueueMessage;
import com.hari134.api_gateway.service.QueueService;

@Service
@Qualifier("rabbitMQService")
public class RabbitMQServiceImpl implements QueueService {
    private final RabbitTemplate rabbitTemplate;
    private final DeferredResultManager deferredResultManager;

    @Autowired
    public RabbitMQServiceImpl(RabbitTemplate rabbitTemplate, DeferredResultManager deferredResultManager) {
        this.rabbitTemplate = rabbitTemplate;
        this.deferredResultManager = deferredResultManager;
    }

    @Override
    public void sendRequest(String correlationId,SubmissionRequestQueueMessage queueMessage) {
        // Publish the request to the request queue with the correlation ID
        queueMessage.setCorrelationId(correlationId);
        rabbitTemplate.convertAndSend("request-queue", queueMessage, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            message.getMessageProperties().getHeaders().remove("__TypeId__");
            return message;
        });
    }

    @RabbitListener(queues = "response-queue")
    public void processResponse(SubmissionResponseQueueMessage userResponse) {
        String correlationId = userResponse.getCorrelationId();
        DeferredResult<SubmissionResponseQueueMessage> deferredResult = deferredResultManager.removeDeferredResult(correlationId);
        if (deferredResult != null) {
            // Set the response based on the message received
            deferredResult.setResult(userResponse);
        }
    }
}

