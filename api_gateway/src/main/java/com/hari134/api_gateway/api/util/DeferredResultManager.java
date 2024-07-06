package com.hari134.api_gateway.api.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.request.async.DeferredResult;

import com.hari134.api_gateway.dto.api.SubmissionResponseWithWait;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ApplicationScope
public class DeferredResultManager {
    private final Map<String, DeferredResult<SubmissionResponseWithWait>> deferredResultsMap = new ConcurrentHashMap<>();

    public void putDeferredResult(String correlationId, DeferredResult<SubmissionResponseWithWait> deferredResult) {
        deferredResultsMap.put(correlationId, deferredResult);
    }

    public DeferredResult<SubmissionResponseWithWait> removeDeferredResult(String correlationId) {
        return deferredResultsMap.remove(correlationId);
    }
}

