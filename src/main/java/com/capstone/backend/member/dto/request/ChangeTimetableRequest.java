package com.capstone.backend.member.dto.request;

import com.capstone.backend.member.domain.value.Day;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

public record ChangeTimetableRequest(
        @Schema(description = "수정할 시간표 엔트리 ID값", example = "1")
        Long id,
        @Schema(description = "요일", example = "MON / TUE / WED / THU / FRI / SAT / SUN")
        Day day,
        @Schema(description = "시작시간", example = "09:00:00.000000")
        LocalTime startTime,
        @Schema(description = "끝 시간", example = "11:00:00.000000")
        LocalTime endTime,
        @Schema(description = "이벤트 이름", example = "분산시스템과컴퓨팅")
        String eventName,
        @Schema(description = "이벤트 상세", example = "신공1201")
        String eventDetail,
        @Schema(description = "color", example = "#f6f6f6")
        String color
) {

}
