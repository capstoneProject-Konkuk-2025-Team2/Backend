package com.capstone.backend.core.common.web.response.exception;

import com.capstone.backend.core.common.web.response.MessageResolver;

public record ApiErrorMessage(
        String code,
        Control control,
        String resolved,
        Object[] args
) {
    public enum Control {
        EXPOSE_IF_RESOLVED, HIDE
    }

    public ApiErrorMessage {
        // 기본 control 값 설정
        if (control == null) control = Control.EXPOSE_IF_RESOLVED;
    }

    public static ApiErrorMessage of(String code) {
        return new ApiErrorMessage(code, Control.EXPOSE_IF_RESOLVED, MessageResolver.resolve(code), null);
    }

    public static ApiErrorMessage of(String code, Control control) {
        return new ApiErrorMessage(code, control, MessageResolver.resolve(code), null);
    }

    public static ApiErrorMessage of(String code, Control control, Object[] args) {
        return new ApiErrorMessage(code, control, MessageResolver.resolve(code, args), args);
    }
}
