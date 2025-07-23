package com.capstone.backend.core.common.web.response.exception;

import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("capstone.common.invalid.request");

        ErrorCodeResolvingApiErrorException exception = new ErrorCodeResolvingApiErrorException(ExtendedHttpStatus.BAD_REQUEST, message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(exception.getError()));
    }
}
