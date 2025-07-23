package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteScheduleRequest(
        @Schema(description = "삭제할 스케쥴 Id", example = "1")
        Long deleteScheduleId
) {

}
