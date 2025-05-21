package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record VerifyAuthCodeRequest (
        @Schema(description = "확인할 이메일", example = "abc@konkuk.ac.kr")
        String email,
        @Schema(description = "인증코드", example = "123456")
        String code
){
}
