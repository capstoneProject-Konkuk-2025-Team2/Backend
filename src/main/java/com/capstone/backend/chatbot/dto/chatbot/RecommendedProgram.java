package com.capstone.backend.chatbot.dto.chatbot;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecommendedProgram(
        @JsonProperty("extracurricular_id")
        Long extracurricularId,
        @JsonProperty("title")
        String title,
        @JsonProperty("score")
        Double score,
        @JsonProperty("question_similarity")
        Double questionSimilarity,
        @JsonProperty("interest_similarity")
        Double interestSimilarity,
        @JsonProperty("kum_mileage")
        Long kum_mileage
) {

}
