package com.capstone.backend.member.dto.response;

import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.value.AcademicStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record LookupMemberInfoResponse(
        @Schema(description = "member PK 값(유저 ID)", example = "1")
        Long id,
        @Schema(description = "이름", example = "김철수")
        String name,
        @Schema(description = "email 정보", example = "abc@def.com")
        String email,
        @Schema(description = "학적 정보", example = "ENROLLED / LEAVE_OF_ABSENCE / GRADUATED")
        AcademicStatus academicStatus,
        @Schema(description = "대학 정보", example = "공과대학")
        String college,
        @Schema(description = "학과 정보", example = "컴퓨터공학부")
        String department,
        @Schema(description = "학년 정보", example = "1")
        Long grade
) {
        public static LookupMemberInfoResponse of(
                Member member
        ) {
                return new LookupMemberInfoResponse(
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getAcademicStatus(),
                        member.getCollege(),
                        member.getDepartment(),
                        member.getGrade()
                );
        }
}
