package com.capstone.backend.core.infrastructure.exception;

import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import com.capstone.backend.core.common.web.response.exception.ErrorCodeResolvingApiErrorException;

public class CustomException extends ErrorCodeResolvingApiErrorException {

    // 기본 code만 전달하는 생성자
    public CustomException() {
        super(ExtendedHttpStatus.BAD_REQUEST, "bad-request");
    }

    // code를 명시적으로 전달하는 생성자
    public CustomException(String code) {
        super(ExtendedHttpStatus.BAD_REQUEST, code);
    }

    public CustomException(ExtendedHttpStatus extendedHttpStatus, String code) {
        super(extendedHttpStatus, code);
    }

    // code + cause 둘 다 전달하는 생성자
    public CustomException(String code, Throwable cause) {
        super(ExtendedHttpStatus.BAD_REQUEST, code, cause);
    }

    // cause만 전달하는 경우 (code는 기본값 사용)
    public CustomException(Throwable cause) {
        super(ExtendedHttpStatus.BAD_REQUEST, "bad-request", cause);
    }
}
