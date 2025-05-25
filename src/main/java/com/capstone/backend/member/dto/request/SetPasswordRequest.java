package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SetPasswordRequest(
   @Schema(description = "패스워드 설정", example = "password1234@#$")
   String password
) {
}
