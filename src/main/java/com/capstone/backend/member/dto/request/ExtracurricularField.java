package com.capstone.backend.member.dto.request;

import com.capstone.backend.core.customAnnotation.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@ValidDateRange
public record ExtracurricularField(
        @Schema(description = "비교과 이름", example = "A비교과")
        String originTitle,
        @Schema(description = "비교과 url", example = "https://abc.com")
        String url,
        @Schema(description = "신청시작 일자", example = "2025-08-01T09:00:00")
        LocalDateTime applicationStart,
        @Schema(description = "신청종료 일자", example = "2025-08-02T09:00:00")
        LocalDateTime applicationEnd,
        @Schema(description = "활동시작 일자", example = "2025-08-07T09:00:00")
        LocalDateTime activityStart,
        @Schema(description = "활동종료 일자", example = "2025-08-07T12:00:00")
        LocalDateTime activityEnd
) {

}
