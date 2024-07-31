package com.hari134.api_gateway.api.controllers;

import com.hari134.api_gateway.dto.queue.SubmissionRequestQueueMessage;
import com.hari134.api_gateway.dto.api.requests.SubmissionRequest;
import com.hari134.api_gateway.dto.api.responses.ApiResponse;
import com.hari134.api_gateway.dto.api.responses.SubmissionResponseWithoutWait;
import com.hari134.api_gateway.dto.queue.SubmissionResponseQueueMessage;
import com.hari134.api_gateway.entity.CodeSubmission;
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
import javax.ws.rs.QueryParam;

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
    public Object sendRequest(@RequestBody SubmissionRequest request,
            @RequestParam(required = false, defaultValue = "false") Boolean wait,
            HttpServletRequest httpRequest) {

        if (wait) {
            return handleSyncRequest(request, httpRequest);
        } else {
            return handleAsyncRequest(request, httpRequest);
        }
    }

    private SubmissionResponseWithoutWait handleAsyncRequest(SubmissionRequest request,
            HttpServletRequest httpRequest) {
        String correlationId = UUID.randomUUID().toString();
        SecurityContext context = SecurityContextHolder.getContext();
        User user = (User) context.getAuthentication().getPrincipal();
        String apiKey = httpRequest.getHeader("X-API-KEY");
        Long apiKeyId = apiKeyService.getApiKeyIdByApiKey(apiKey);

        codeSubmissionService.saveOnAsyncSubmissionRequestInitialization(
                correlationId,
                user.getUserId(),
                apiKeyId,
                request.getSourceCode(),
                request.getStdInput(),
                request.getExpectedOutput(),
                request.getMemoryLimit(),
                request.getTimeLimit(),
                request.getWallTimeLimit());
        SubmissionRequestQueueMessage submissionRequestQueueMessage = SubmissionRequestQueueMessage
                .fromSubmissionRequest(request, false);
        queueService.sendRequest(correlationId, submissionRequestQueueMessage);

        // Return the correlation ID immediately
        return new SubmissionResponseWithoutWait(correlationId);
    }

    private DeferredResult<SubmissionResponseQueueMessage> handleSyncRequest(SubmissionRequest request,
            HttpServletRequest httpRequest) {
        String correlationId = UUID.randomUUID().toString();
        DeferredResult<SubmissionResponseQueueMessage> deferredResult = new DeferredResult<>();
        deferredResultManager.putDeferredResult(correlationId, deferredResult);
        SubmissionRequestQueueMessage submissionRequestQueueMessage = SubmissionRequestQueueMessage
                .fromSubmissionRequest(request, true);
        queueService.sendRequest(correlationId, submissionRequestQueueMessage);

        SecurityContext context = SecurityContextHolder.getContext();

        deferredResult.onTimeout(() -> {
            deferredResultManager.removeDeferredResult(correlationId);
            deferredResult.setErrorResult(new RuntimeException("Response not received in time."));
        });

        deferredResult.onCompletion(new DelegatingSecurityContextRunnable(() -> {
            if (deferredResult.getResult() != null) {
                try {
                    SubmissionResponseQueueMessage response = (SubmissionResponseQueueMessage) deferredResult
                            .getResult();

                    if (response.getError() != null && !response.getError().isEmpty()) {
                        deferredResult.setErrorResult(
                                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));
                    } else {
                        User user = (User) context.getAuthentication().getPrincipal();
                        String apiKey = httpRequest.getHeader("X-API-KEY");
                        Long apiKeyId = apiKeyService.getApiKeyIdByApiKey(apiKey);

                        codeSubmissionService.saveSubmissionResponse(response,
                                correlationId,
                                user.getUserId(),
                                apiKeyId,
                                request.getSourceCode(),
                                request.getStdInput(),
                                request.getExpectedOutput(),
                                request.getMemoryLimit(),
                                request.getTimeLimit(),
                                request.getWallTimeLimit());
                    }
                } catch (Exception e) {
                    deferredResult.setErrorResult(
                            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e));
                }
            }
        }, context));

        return deferredResult;
    }

    @GetMapping("/{correlationId}")
    public Object getSubmissionStatus(@PathVariable String correlationId) {
        CodeSubmission submission = codeSubmissionService.findByCorrelationId(correlationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found"));

        SubmissionResponseQueueMessage response = new SubmissionResponseQueueMessage();
        response.setCorrelationId(correlationId);
        response.setStatus(submission.getMessage());

        if (submission.isSubmissionComplete()) {
            response.setOutput(submission.getStdout());
            response.setMemoryUsed(String.valueOf(submission.getMemory()));
            response.setCpuTime(String.valueOf(submission.getCpuTime()));
            response.setWallTime(String.valueOf(submission.getWallTime()));
            response.setExitCode(String.valueOf(submission.getExitStatus()));
            return new ApiResponse<>(true, "Submission completed", response);
        } else {
            response.setStatus("Processing");
            return new ApiResponse<>(true, "Submission is still processing", response);
        }
    }
}
