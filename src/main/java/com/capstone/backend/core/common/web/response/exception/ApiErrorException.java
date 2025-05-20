package com.capstone.backend.core.common.web.response.exception;

import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException {
    private final ApiError error;

    public ApiErrorException(ApiError error) {
        super();
        this.error = error;
    }

    public ApiErrorException(ApiError error, Throwable cause) {
        super(cause);
        this.error = error;
    }

}
