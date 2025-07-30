package com.capstone.backend.member.dto.response;

import com.capstone.backend.member.domain.entity.Extracurricular;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.value.ScheduleType;
import com.capstone.backend.member.dto.request.ExtracurricularField;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Optional;

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
        LocalDate endDate,
        @Schema(description = "관련된 비교과(일반 일정일 경우에는 null값이 들어옴)")
        ExtracurricularField extracurricularField
) {
    public static GetScheduleDetailResponse of(
            Schedule schedule,
            Extracurricular extracurricular
    ) {
        return new GetScheduleDetailResponse(
                schedule.getTitle(),
                schedule.getContent(),
                extracurricular == null ? ScheduleType.NORMAL : ScheduleType.EXTRACURRICULAR,
                schedule.getStartDate(),
                schedule.getEndDate(),
                Optional.ofNullable(extracurricular)
                        .map(e -> new ExtracurricularField(
                                e.getTitle(),
                                e.getUrl(),
                                e.getApplicationStart(),
                                e.getApplicationEnd(),
                                e.getActivityStart(),
                                e.getActivityEnd()
                        ))
                        .orElse(null)
        );
    }
}
