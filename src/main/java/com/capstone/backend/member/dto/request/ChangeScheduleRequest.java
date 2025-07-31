package com.capstone.backend.member.dto.request;

import com.capstone.backend.core.customAnnotation.ValidDateRange;
import com.capstone.backend.member.domain.value.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
@ValidDateRange
public record ChangeScheduleRequest(
        @NotNull(message = "capstone.schedule.id.blank")
        @Schema(description = "수정할 스케쥴 id(pk값)", example = "1")
        Long scheduleId,
        @NotBlank(message = "capstone.schedule.content.blank")
        @Schema(description = "스케줄 제목", example = "백엔드 비교과 신청")
        String title,
        @NotBlank(message = "capstone.schedule.type.blank")
        @Schema(description = "스케쥴 상세정보", example = "공C487에서 열릴 예정, 시간은 17시 ~ 19시")
        String content,
        @NotNull(message = "capstone.schedule.start.date.blank")
        @Schema(description = "시작 일자", example = "2025-07-19")
        LocalDate startDate,

        @NotNull(message = "capstone.schedule.end.date.blank")
        @Schema(description = "끝 일자", example = "2025-07-21")
        LocalDate endDate,

        @Schema(description = "관련된 비교과(일반 일정일 경우에는 null값으로)")
        ExtracurricularField extracurricularField
) {

}
