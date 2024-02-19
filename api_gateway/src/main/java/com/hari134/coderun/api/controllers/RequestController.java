package com.hari134.coderun.api.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import com.hari134.coderun.api.dto.api.UserRequest;
import com.hari134.coderun.api.dto.api.UserResponse;
import com.hari134.coderun.api.util.DeferredResultManager;
import com.hari134.coderun.queue.RequestProducer;;

@RestController
public class RequestController {
    private RequestProducer requestProducer;
    private DeferredResultManager deferredResultManager;

    @Autowired
    public RequestController(RequestProducer requestProducer, DeferredResultManager deferredResultManager) {
        this.requestProducer = requestProducer;
        this.deferredResultManager = deferredResultManager;
    }

    @PostMapping("/execute")
    public DeferredResult<UserResponse> sendRequest(@RequestBody UserRequest request) {
        // Generate a unique correlation ID for the request
        String correlationId = UUID.randomUUID().toString();

        DeferredResult<UserResponse> deferredResult = new DeferredResult<>();
        deferredResultManager.putDeferredResult(correlationId, deferredResult);

        requestProducer.SendRequest(request, correlationId);
        // Set a timeout handler for the DeferredResult in case the response does not
        // arrive in time
        deferredResult.onTimeout(() -> {
            deferredResultManager.removeDeferredResult(correlationId);
            deferredResult.setErrorResult(new RuntimeException("Response not received in time."));
        });

        deferredResult.onCompletion(() -> {
            if (deferredResult.getResult() != null && ((UserResponse) deferredResult.getResult()).getError() != null) {
                deferredResult.setErrorResult(
                        new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));
            }
        });

        return deferredResult;
    }
}
