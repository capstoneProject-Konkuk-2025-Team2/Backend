package com.capstone.backend.member.dto.response;

import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.value.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record GetScheduleByYearAndMonthResponse(
        @Schema(description = "스케쥴 id", example = "1")
        Long scheduleId,
        @Schema(description = "스케쥴 제목", example = "A비교과")
        String title,
        @Schema(description = "스케쥴 타입", example = "EXTRACURRICULAR(비교과 관련), NORMAL(일반 일정)")
        ScheduleType scheduleType,
        @Schema(description = "시작 날짜", example = "2025-07-19")
        LocalDate startDate,
        @Schema(description = "끝 날짜", example = "2025-07-20")
        LocalDate endDate
) {
        public static GetScheduleByYearAndMonthResponse of(
                Schedule schedule
        ) {
                return new GetScheduleByYearAndMonthResponse(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getScheduleType(),
                        schedule.getStartDate(),
                        schedule.getEndDate()
                );
        }
}
