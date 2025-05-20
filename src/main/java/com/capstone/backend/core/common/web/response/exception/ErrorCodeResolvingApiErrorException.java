package com.capstone.backend.core.common.web.response.exception;

import com.capstone.backend.core.common.support.SpringContextHolder;
import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import com.capstone.backend.core.configuration.env.AppEnv;

public class ErrorCodeResolvingApiErrorException extends ApiErrorException {

    private static final AppEnv appEnv;

    static {
        appEnv = SpringContextHolder.getBean(AppEnv.class);
    }

    public ErrorCodeResolvingApiErrorException(ExtendedHttpStatus statusCode, String code) {
        super(ApiError.of(ApiErrorElement.of(
                appEnv.getId(),
                statusCode,
                ApiErrorCode.of(code),
                ApiErrorMessage.of(code)
        )));
    }

    public ErrorCodeResolvingApiErrorException(ExtendedHttpStatus statusCode, String code, Throwable cause) {
        super(ApiError.of(ApiErrorElement.of(
                appEnv.getId(),
                statusCode,
                ApiErrorCode.of(code),
                ApiErrorMessage.of(code)
        )), cause);
    }

    public ErrorCodeResolvingApiErrorException(ExtendedHttpStatus statusCode, String code, Object[] args) {
        super(ApiError.of(ApiErrorElement.of(
                appEnv.getId(),
                statusCode,
                ApiErrorCode.of(code),
                ApiErrorMessage.of(code, ApiErrorMessage.Control.EXPOSE_IF_RESOLVED, args)
        )));
    }

    public ErrorCodeResolvingApiErrorException(ExtendedHttpStatus statusCode, String code, Object[] args, Throwable cause) {
        super(ApiError.of(ApiErrorElement.of(
                appEnv.getId(),
                statusCode,
                ApiErrorCode.of(code),
                ApiErrorMessage.of(code, ApiErrorMessage.Control.EXPOSE_IF_RESOLVED, args)
        )), cause);
    }

    public ErrorCodeResolvingApiErrorException(ExtendedHttpStatus statusCode, String code, Object[] args, Throwable cause, Object data) {
        super(ApiError.of(new ApiErrorElement(
                appEnv.getId(),
                statusCode,
                ApiErrorCode.of(code),
                ApiErrorMessage.of(code, ApiErrorMessage.Control.EXPOSE_IF_RESOLVED, args),
                data
        )), cause);
    }
}
