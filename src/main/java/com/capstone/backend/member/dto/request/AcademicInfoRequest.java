package com.capstone.backend.member.dto.request;

import com.capstone.backend.member.domain.value.AcademicStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AcademicInfoRequest(
    @NotNull(message = "capstone.academic.info.blank")
    @Schema(description = "재학상태", example = "ENROLLED / LEAVE_OF_ABSENCE / GRADUATED")
    AcademicStatus academicStatus,
    @NotNull(message = "capstone.academic.info.blank")
    @Schema(description = "학년", example = "1")
    Long grade,
    @NotBlank(message = "capstone.academic.info.blank")
    @Schema(description = "단과대학", example = "공과대학")
    String college,
    @NotBlank(message = "capstone.academic.info.blank")
    @Schema(description = "학과", example = "컴퓨터공학부")
    String department,
    @NotBlank(message = "capstone.academic.info.blank")
    @Schema(description = "이름", example = "홍길동")
    String name
) {
}
