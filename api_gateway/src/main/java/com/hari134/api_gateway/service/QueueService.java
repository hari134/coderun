package com.hari134.api_gateway.service;

import com.hari134.api_gateway.dto.api.SubmissionRequest;
import com.hari134.api_gateway.dto.api.SubmissionResponseWithWait;
import com.hari134.api_gateway.dto.queue.SubmissionRequestQueueMessage;

public interface QueueService {
    void sendRequest(SubmissionRequestQueueMessage queueMessage);
    void processResponse(String correlationId, SubmissionResponseWithWait userResponse);
}

