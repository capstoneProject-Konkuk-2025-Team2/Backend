package com.capstone.backend.extracurricular.dto.response;

import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record ExtracurricularResponse(
        @Schema(description = "비교과 id", example = "1")
        Long id,
        @Schema(description = "비교과 제목", example = "온라인 학습법 특강(AI)")
        String title,
        @Schema(description = "비교과 url", example = "https://abc.cdf")
        String url,
        @Schema(description = "비교과 등록 시작일", example = "2025-07-15T14:00:00")
        LocalDateTime applicationStart,
        @Schema(description = "비교과 등록 마감일", example = "2025-07-17T14:00:00")
        LocalDateTime applicationEnd,
        @Schema(description = "비교과 활동 시작일", example = "2025-07-20T14:00:00")
        LocalDateTime activityStart,
        @Schema(description = "비교과 활동 마감일", example = "2025-07-20T17:00:00")
        LocalDateTime activityEnd
) {
    public static ExtracurricularResponse of(
            Extracurricular extracurricular
    ) {
        return new ExtracurricularResponse(
                extracurricular.getExtracurricularId(),
                extracurricular.getTitle(),
                extracurricular.getUrl(),
                extracurricular.getApplicationStart(),
                extracurricular.getApplicationEnd(),
                extracurricular.getActivityStart(),
                extracurricular.getActivityEnd()
        );
    }
}
