package com.hari134.api_gateway.api.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hari134.api_gateway.entity.User;
import com.hari134.api_gateway.service.impl.ApiKeyService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    public ApiKeyAuthFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey != null && apiKeyService.validateApiKey(apiKey)) {
            User user = apiKeyService.getUserFromApiKey(apiKey);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
