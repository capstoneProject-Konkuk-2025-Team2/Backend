package com.capstone.backend.core.common.web.response;

public enum ExtendedHttpStatus {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503);

    private final int code;

    ExtendedHttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isClientError() {
        return code >= 400 && code <= 499;
    }

    public boolean isServerError() {
        return code >= 500 && code <= 599;
    }

    public boolean isError() {
        return isClientError() || isServerError();
    }

    @Override
    public String toString() {
        return Integer.toString(code);
    }

    public static ExtendedHttpStatus fromCode(int code) {
        for (ExtendedHttpStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
