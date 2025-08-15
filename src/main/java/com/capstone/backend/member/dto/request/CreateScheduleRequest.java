package com.capstone.backend.member.dto.request;

import com.capstone.backend.core.customAnnotation.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@ValidDateRange
public record CreateScheduleRequest(
        @NotBlank(message = "capstone.schedule.title.blank")
        @Schema(description = "스케줄 제목", example = "백엔드 비교과 신청")
        String title,

        @NotBlank(message = "capstone.schedule.content.blank")
        @Schema(description = "스케쥴 상세정보", example = "공C487에서 열릴 예정, 시간은 17시 ~ 19시")
        String content,

        @Schema(description = "시작 일자(비교과 관련 일정이면 null로)", example = "2025-07-19T14:30:45")
        LocalDateTime startDate,

        @Schema(description = "끝 일자(비교과 관련 일정이면 null로)", example = "2025-07-21T14:30:45")
        LocalDateTime endDate,

        @Schema(description = "관련된 비교과 id", example = "1")
        Long extracurricularId
) {

}
