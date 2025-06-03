package com.capstone.backend.chatbot.dto.chatbot.request;

import com.capstone.backend.member.domain.entity.Member;

public record ChatbotQuestionRequest(
        Long id,
        String question
) {
    public static ChatbotQuestionRequest of(
            Member member,
            String question
    ) {
        return new ChatbotQuestionRequest(
                2L, //Todo 외부 api 오류 해결시 member.getId로 고치기
                question
        );
    }
}
