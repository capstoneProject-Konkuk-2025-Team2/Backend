package com.capstone.backend.chatbot.presentation;

import com.capstone.backend.chatbot.dto.chatbot.response.ChatbotQuestionResponse;
import com.capstone.backend.chatbot.dto.our.request.QuestionToChatServerRequest;
import com.capstone.backend.chatbot.dto.our.response.QuestionToChatServerResponse;
import com.capstone.backend.chatbot.facade.ChatbotFacade;
import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.web.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "챗봇 관련 API", description = "ChatbotController")
public class ChatbotController {
    private final ChatbotFacade chatbotFacade;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.CHAT_REGISTER)
    @Operation(summary = "챗봇에 사용자 정보 등록", description = "registerMemberInfo")
    public ApiResponse<Boolean> registerMemberInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ApiResponse.success(chatbotFacade.registerMemberInfo(customUserDetails));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(ApiPath.CHAT_REQUEST)
    @Operation(summary = "챗봇에 질문", description = "questionToChatServer")
    public ApiResponse<QuestionToChatServerResponse> questionToChatServer(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody QuestionToChatServerRequest questionToChatServerRequest
    ) {
        return ApiResponse.success(chatbotFacade.questionToChatServer(customUserDetails, questionToChatServerRequest));
    }
}
