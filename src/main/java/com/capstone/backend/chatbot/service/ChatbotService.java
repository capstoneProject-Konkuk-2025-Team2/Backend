package com.capstone.backend.chatbot.service;

import com.capstone.backend.chatbot.dto.request.ChatbotRegisterRequest;
import com.capstone.backend.chatbot.presentation.ChatbotApiPath;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.core.infrastructure.externalAPI.dto.ExternalApiResponse;
import com.capstone.backend.core.infrastructure.externalAPI.service.ExternalApiRequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {
    private static final Logger log = LoggerFactory.getLogger(ChatbotService.class.getName());
    private final ExternalApiRequestService externalApiRequestService;

    @Value("${spring.chatbot.host}")
    private String chatbotApiHost;

    public void register(ChatbotRegisterRequest chatbotRegisterRequest) {
        ResponseEntity<ExternalApiResponse> response = externalApiRequestService.post(
                chatbotApiHost + ChatbotApiPath.REGISTER , chatbotRegisterRequest, ExternalApiResponse.class
        );
        log.info(response.getBody().message());
        if(response.getBody().code() != 200) {
            throw new CustomException(response.getBody().message());
        }
    }
}
