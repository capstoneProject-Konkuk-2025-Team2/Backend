package com.capstone.backend.core.common.web.response.exception;

import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;

public record ApiErrorElement(
        String appId,
        ExtendedHttpStatus status,
        ApiErrorCode code,
        ApiErrorMessage message,
        Object data
) {
    public static ApiErrorElement of(String appId, ExtendedHttpStatus status, ApiErrorCode code, ApiErrorMessage message) {
        return new ApiErrorElement(appId, status, code, message, null);
    }
}
