package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FcmTokenRequest(
        @Schema(description = "fcm token", example = "abc")
        String fcmToken
) {

}
