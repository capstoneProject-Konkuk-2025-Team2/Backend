package com.capstone.backend.core.common.web.response.exception;

public record ApiError(ApiErrorElement element) {
    public static ApiError of(ApiErrorElement element) {
        return new ApiError(element);
    }
}
