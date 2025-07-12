package com.capstone.backend.core.common.web.response.exception;

import com.capstone.backend.core.common.support.SpringContextHolder;
import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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
        BindingResult result = ex.getBindingResult();
        StringBuilder errMessage = new StringBuilder();
        for (FieldError error : result.getFieldErrors()) {
            errMessage.append("[")
                    .append(error.getField())
                    .append("] ")
                    .append(":")
                    .append(error.getDefaultMessage());
        }
        return new ResponseEntity<>(errMessage , HttpStatus.BAD_REQUEST);
    }
}
