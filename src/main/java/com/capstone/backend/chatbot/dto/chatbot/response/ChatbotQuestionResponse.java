package com.capstone.backend.chatbot.dto.chatbot.response;

import com.capstone.backend.chatbot.dto.chatbot.RecommendedProgram;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ChatbotQuestionResponse (
        @JsonProperty("answer")
        String answer,
        @JsonProperty("recommendations")
        List<RecommendedProgram> recommendations,
        @JsonProperty("recommended_ids")
        List<Long> recommended_ids
) {
}
