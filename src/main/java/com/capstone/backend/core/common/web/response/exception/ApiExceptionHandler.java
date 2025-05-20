package com.capstone.backend.core.common.web.response.exception;

import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiErrorException(ApiErrorException ex) {
        ExtendedHttpStatus status = ex.getError().element().status();
        return ResponseEntity
                .status(status.getCode())
                .body(ApiResponse.error(ex.getError()));
    }
}
