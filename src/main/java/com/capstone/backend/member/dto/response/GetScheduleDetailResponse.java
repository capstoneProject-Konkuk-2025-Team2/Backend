package com.capstone.backend.member.dto.response;

import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.value.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record GetScheduleDetailResponse(
        @Schema(description = "스케쥴 제목", example = "A비교과")
        String title,
        @Schema(description = "스케쥴 상세정보", example = "공C487에서 열릴 예정, 시간은 17시 ~ 19시")
        String content,
        @Schema(description = "스케쥴 타입", example = "EXTRACURRICULAR(비교과 관련), NORMAL(일반 일정)")
        ScheduleType scheduleType,
        @Schema(description = "시작 날짜", example = "2025-07-19")
        LocalDate startDate,
        @Schema(description = "끝 날짜", example = "2025-07-20")
        LocalDate endDate
) {
    public static GetScheduleDetailResponse of(
            Schedule schedule
    ) {
        return new GetScheduleDetailResponse(
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getScheduleType(),
                schedule.getStartDate(),
                schedule.getEndDate()
        );
    }
}
