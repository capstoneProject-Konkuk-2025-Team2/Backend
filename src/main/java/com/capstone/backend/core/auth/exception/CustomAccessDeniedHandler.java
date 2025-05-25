package com.capstone.backend.core.auth.exception;

import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import com.capstone.backend.core.common.web.response.exception.ErrorCodeResolvingApiErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        System.out.println("handle");
        Pair<ExtendedHttpStatus,String> error = Pair.of(ExtendedHttpStatus.FORBIDDEN,"capstone.common.invalid.role");
        ErrorCodeResolvingApiErrorException exception = new ErrorCodeResolvingApiErrorException(
                error.getLeft(),
                error.getRight()
        );
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(exception));
    }
}