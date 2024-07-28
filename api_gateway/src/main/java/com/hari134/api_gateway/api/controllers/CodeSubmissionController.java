package com.hari134.api_gateway.api.controllers;

import com.hari134.api_gateway.dto.queue.SubmissionRequestQueueMessage;
import com.hari134.api_gateway.dto.api.requests.SubmissionRequest;
import com.hari134.api_gateway.dto.queue.SubmissionResponseQueueMessage;
import com.hari134.api_gateway.entity.User;
import com.hari134.api_gateway.service.impl.CodeSubmissionService;
import com.hari134.api_gateway.service.QueueService;
import com.hari134.api_gateway.api.util.DeferredResultManager;
import com.hari134.api_gateway.service.impl.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/submissions")
public class CodeSubmissionController {


    @Autowired
    private QueueService queueService;

    @Autowired
    private DeferredResultManager deferredResultManager;

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    @Autowired
    private ApiKeyService apiKeyService;

    @PostMapping("/execute")
    public DeferredResult<SubmissionResponseQueueMessage> sendRequest(@RequestBody SubmissionRequest request, HttpServletRequest httpRequest) {
        // Generate a unique correlation ID for the request
        String correlationId = UUID.randomUUID().toString();
        DeferredResult<SubmissionResponseQueueMessage> deferredResult = new DeferredResult<>();
        deferredResultManager.putDeferredResult(correlationId, deferredResult);
        SubmissionRequestQueueMessage submissionRequestQueueMessage = SubmissionRequestQueueMessage.fromSubmissionRequest(request);
        queueService.sendRequest(correlationId, submissionRequestQueueMessage);

        // Get the current security context
        SecurityContext context = SecurityContextHolder.getContext();

        // Set a timeout handler for the DeferredResult in case the response does not arrive in time
        deferredResult.onTimeout(() -> {
            deferredResultManager.removeDeferredResult(correlationId);
            deferredResult.setErrorResult(new RuntimeException("Response not received in time."));
        });

        // Wrap the onCompletion callback in a DelegatingSecurityContextRunnable
        deferredResult.onCompletion(new DelegatingSecurityContextRunnable(() -> {
            if (deferredResult.getResult() != null) {
                try {
                    SubmissionResponseQueueMessage response = (SubmissionResponseQueueMessage) deferredResult.getResult();

                    if (response.getError() != null && !response.getError().isEmpty()) {
                        deferredResult.setErrorResult(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));
                    } else {
                        // Retrieve the authenticated user and API key
                        User user = (User) context.getAuthentication().getPrincipal();
                        String apiKey = httpRequest.getHeader("X-API-KEY");
                        Long apiKeyId = apiKeyService.getApiKeyIdByApiKey(apiKey);

                        // Save the response to the database
                        codeSubmissionService.saveSubmissionResponse(response,
                                correlationId,
                                user.getUserId(),
                                apiKeyId,
                                request.getSourceCode(),
                                request.getStdInput(),
                                request.getExpectedOutput(),
                                request.getTimeLimit(),
                                request.getMemoryLimit(),
                                request.getTimeLimit(),
                                request.getWallTimeLimit());

                    }
                } catch (Exception e) {
                    deferredResult.setErrorResult(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e));
                }
            }
        }, context));

        return deferredResult;
    }
}
