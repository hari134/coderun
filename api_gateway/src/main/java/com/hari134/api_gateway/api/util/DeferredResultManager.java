package com.hari134.api_gateway.api.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.request.async.DeferredResult;

import com.hari134.api_gateway.dto.queue.SubmissionResponseQueueMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ApplicationScope
public class DeferredResultManager {
    private final Map<String, DeferredResult<SubmissionResponseQueueMessage>> deferredResultsMap = new ConcurrentHashMap<>();

    public void putDeferredResult(String correlationId, DeferredResult<SubmissionResponseQueueMessage> deferredResult) {
        deferredResultsMap.put(correlationId, deferredResult);
    }

    public DeferredResult<SubmissionResponseQueueMessage> removeDeferredResult(String correlationId) {
        return deferredResultsMap.remove(correlationId);
    }
}

