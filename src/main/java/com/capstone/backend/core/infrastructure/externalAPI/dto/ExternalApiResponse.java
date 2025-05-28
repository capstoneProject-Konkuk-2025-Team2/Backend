package com.capstone.backend.core.infrastructure.externalAPI.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record ExternalApiResponse(
        int code,
        String message,
        JsonNode data
) {
    public boolean isSuccess() {
        return code == 200;
    }
}
