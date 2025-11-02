package com.capstone.backend.alarm.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FCMMessage(
        boolean validateOnly,
        Message message
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Message(
            Notification notification,
            String token,
            Webpush webpush
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Notification(
            String title,
            String body,
            String image
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Webpush(
            @JsonProperty("fcm_options")
            FcmOptions fcmOptions
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FcmOptions(
            String link
    ) {}
}
