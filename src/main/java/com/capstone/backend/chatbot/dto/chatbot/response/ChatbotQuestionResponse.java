package com.capstone.backend.chatbot.dto.chatbot.response;

import com.capstone.backend.chatbot.dto.chatbot.RecommendedProgram;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ChatbotQuestionResponse(
        @JsonProperty("user_summary")
        String userSummary,
        @JsonProperty("recommendation_intro")
        String recommendationInfo,
        @JsonProperty("answer")
        String answer,
        @JsonProperty("total_programs")
        Long total_programs,
        @JsonProperty("recommended_programs")
        List<RecommendedProgram> recommendedPrograms
) {
}
