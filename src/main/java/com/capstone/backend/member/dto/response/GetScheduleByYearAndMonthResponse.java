package com.capstone.backend.member.dto.response;

import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.value.ScheduleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record GetScheduleByYearAndMonthResponse(
        @Schema(description = "스케쥴 id", example = "1")
        Long scheduleId,
        @Schema(description = "스케쥴 제목", example = "A비교과")
        String title,
        @Schema(description = "스케쥴 타입", example = "EXTRACURRICULAR(비교과 관련), NORMAL(일반 일정)")
        ScheduleType scheduleType,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "시작 날짜", example = "2025-07-19T14:30:45")
        LocalDateTime startDateTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "끝 날짜", example = "2025-07-20T14:30:45")
        LocalDateTime endDateTime,

        @Schema(description = "알림 설정 여부", example = "true")
        Boolean isAlarm
) {
        public static GetScheduleByYearAndMonthResponse of(
                Schedule schedule
        ) {
                ScheduleType type = (schedule.getExtracurricularId() == null)
                        ? ScheduleType.NORMAL
                        : ScheduleType.EXTRACURRICULAR;
                return new GetScheduleByYearAndMonthResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        type,
                        schedule.getStartDateTime(),
                        schedule.getEndDateTime(),
                        schedule.getIsAlarm()
                );
        }
}
