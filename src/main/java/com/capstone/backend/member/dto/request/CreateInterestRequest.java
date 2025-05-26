package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateInterestRequest(
        @Schema(description = "관심사항", example = "웹 개발")
        String interestContent
) {

}
