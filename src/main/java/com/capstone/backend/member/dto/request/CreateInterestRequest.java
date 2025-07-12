package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateInterestRequest(
        @NotBlank(message = "관심사항은 필수 입력값입니다.")
        @Schema(description = "관심사항", example = "웹 개발, AI, 백엔드")
        String interestContent
) {

}
