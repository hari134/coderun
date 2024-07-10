package com.hari134.api_gateway.service;

import com.hari134.api_gateway.dto.api.SubmissionRequest;
import com.hari134.api_gateway.dto.queue.SubmissionRequestQueueMessage;
import com.hari134.api_gateway.dto.queue.SubmissionResponseQueueMessage;

public interface QueueService {
    void sendRequest(String correlationId,SubmissionRequestQueueMessage queueMessage);
    void processResponse(SubmissionResponseQueueMessage userResponse);
}

