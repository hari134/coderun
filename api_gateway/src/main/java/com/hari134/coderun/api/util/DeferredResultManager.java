package com.hari134.coderun.api.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.request.async.DeferredResult;

import com.hari134.coderun.api.dto.api.UserResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ApplicationScope
public class DeferredResultManager {
    private final Map<String, DeferredResult<UserResponse>> deferredResultsMap = new ConcurrentHashMap<>();

    public void putDeferredResult(String correlationId, DeferredResult<UserResponse> deferredResult) {
        deferredResultsMap.put(correlationId, deferredResult);
    }

    public DeferredResult<UserResponse> removeDeferredResult(String correlationId) {
        return deferredResultsMap.remove(correlationId);
    }
}

