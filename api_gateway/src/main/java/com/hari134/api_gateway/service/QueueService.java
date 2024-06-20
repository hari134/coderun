package com.hari134.api_gateway.service;

import com.hari134.api_gateway.dto.api.UserRequest;
import com.hari134.api_gateway.dto.api.UserResponse;

public interface QueueService {
    void sendRequest(UserRequest userRequest, String correlationId);
    void processResponse(String correlationId, UserResponse userResponse);
}

