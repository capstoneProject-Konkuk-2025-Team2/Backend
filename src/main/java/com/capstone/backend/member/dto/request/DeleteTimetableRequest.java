package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DeleteTimetableRequest(
        @NotNull(message = "capstone.timetable.not.found")
        @Schema(description = "삭제할 시간표 Id", example = "1")
        Long deleteTimetableId
) {

}
