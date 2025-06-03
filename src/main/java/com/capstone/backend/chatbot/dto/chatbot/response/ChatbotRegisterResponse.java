package com.capstone.backend.chatbot.dto.chatbot.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatbotRegisterResponse(
        @JsonProperty("user_profile")
        UserProfile userProfile
) {

}
