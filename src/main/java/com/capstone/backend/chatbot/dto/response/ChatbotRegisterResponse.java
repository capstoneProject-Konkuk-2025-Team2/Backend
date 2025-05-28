package com.capstone.backend.chatbot.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatbotRegisterResponse(
        @JsonProperty("user_profile")
        UserProfile userProfile
) {

}
