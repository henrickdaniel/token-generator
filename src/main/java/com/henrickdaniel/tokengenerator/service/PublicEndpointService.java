package com.henrickdaniel.tokengenerator.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class PublicEndpointService {

    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
            "/api/auth/register",
            "/api/auth/login",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    public static boolean isPublicEndpoint(HttpServletRequest request) {
        log.info("URI: {}", request.getRequestURI());
        return PUBLIC_ENDPOINTS.stream().anyMatch(endpoint -> {
            if (endpoint.endsWith("/**")) {
                String prefixo = endpoint.substring(0, endpoint.length() - 3);
                return request.getRequestURI().startsWith(prefixo);
            } else {
                return endpoint.equals(request.getRequestURI());
            }
        });
    }

}
