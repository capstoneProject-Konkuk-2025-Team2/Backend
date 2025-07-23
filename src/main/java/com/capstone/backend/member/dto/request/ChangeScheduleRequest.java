package com.capstone.backend.member.dto.request;

import com.capstone.backend.core.customAnnotation.ValidDateRange;
import com.capstone.backend.member.domain.value.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
@ValidDateRange
public record ChangeScheduleRequest(
        @NotNull(message = "스케쥴 id를 입력해주세요")
        @Schema(description = "수정할 스케쥴 id(pk값)", example = "1")
        Long scheduleId,
        @NotBlank(message = "스케쥴 제목을 입력해주세요")
        @Schema(description = "스케줄 제목", example = "백엔드 비교과 신청")
        String title,
        @NotNull(message = "타입을 입력해주세요")
        @Schema(description = "스케줄 타입", example = "EXTRACURRICULAR(비교과 관련), NORMAL(일반 일정)")
        ScheduleType scheduleType,
        @NotNull(message = "시작 일자를 입력해주세요")
        @Schema(description = "시작 일자", example = "2025-07-19")
        LocalDate startDate,

        @NotNull(message = "끝 일자를 입력해주세요")
        @Schema(description = "끝 일자", example = "2025-07-21")
        LocalDate endDate
) {

}
