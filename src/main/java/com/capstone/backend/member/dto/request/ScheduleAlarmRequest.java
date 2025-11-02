package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ScheduleAlarmRequest(
        @Schema(description = "스케쥴 id", example = "1")
        Long scheduleId,
        @Schema(description = "알림 설정 여부", example = "true")
        Boolean isAlarm
) {

}
