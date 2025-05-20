package com.capstone.backend.core.common.web.response.exception;

public record ApiErrorCode(String value) {
    public static ApiErrorCode of(String code) {
        return new ApiErrorCode(code);
    }
}
