package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SendAuthMailRequest(
   @Schema(description = "인증할 이메일", example = "abc@konkuk.ac.kr")
   String email
) {
}
