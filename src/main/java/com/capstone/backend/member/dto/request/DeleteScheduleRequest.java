package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DeleteScheduleRequest(
        @NotNull(message = "삭제할 스케쥴 Id를 입력해주세요")
        @Schema(description = "삭제할 스케쥴 Id", example = "1")
        Long deleteScheduleId
) {

}
