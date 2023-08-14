package com.hari134.coderun.queue;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.hari134.coderun.api.dto.api.UserResponse;
import com.hari134.coderun.api.dto.queue.ResponseQueueMessage;
import com.hari134.coderun.api.util.DeferredResultManager;

@Component
public class ResponseConsumer {
    private final DeferredResultManager deferredResultManager;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ResponseConsumer(RabbitTemplate rabbitTemplate, DeferredResultManager deferredResultManager) {
        this.deferredResultManager = deferredResultManager;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "response-queue")
    public void processResponse(Message responseMessage) {
        ResponseQueueMessage queueMessage = (ResponseQueueMessage) rabbitTemplate.getMessageConverter()
                .fromMessage(responseMessage);

        String correlationId = responseMessage.getMessageProperties().getCorrelationId();

        DeferredResult<UserResponse> deferredResult = deferredResultManager.removeDeferredResult(correlationId);
        if (deferredResult != null) {
            // Create and set the response based on the message received
            UserResponse userResponse = createUserResponse(queueMessage);
            deferredResult.setResult(userResponse);
        }
    }

    private UserResponse createUserResponse(ResponseQueueMessage queueMessage) {
        UserResponse userResponse = new UserResponse();
        userResponse.setStdOut(queueMessage.getStdOut());
        userResponse.setStdErr(queueMessage.getStdErr());
    
        // Check if an error message exists and set it
        if (queueMessage.getError() != null) {
            userResponse.setError(queueMessage.getError());
        }
    
        return userResponse;
    }
}
