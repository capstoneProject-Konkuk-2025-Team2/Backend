package com.capstone.backend.member.dto.response;

import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.value.Day;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.Builder;

public record LookupTimetableResponse(
        @Schema(description = "시간표 엔트리 ID(DB PK값)", example = "1")
        Long id,
        @Schema(description = "요일", example = "MON")
        Day day,
        @Schema(description = "색상", example = "#f6f6f6")
        String color,
        @Schema(description = "시간표 제목", example = "운영체제")
        String eventName,
        @Schema(description = "시간표 내용", example = "공C487")
        String eventDetail,
        @Schema(description = "시작 시각", example = "09:00:00.000000")
        LocalTime startTime,
        @Schema(description = "끝 시각", example = "11:00:00.000000")
        LocalTime endTime
) {
    public static LookupTimetableResponse of(Timetable timetable) {
        return new LookupTimetableResponse(
                timetable.getId(),
                timetable.getDay(),
                timetable.getColor(),
                timetable.getEventName(),
                timetable.getEventDetail(),
                timetable.getStartTime(),
                timetable.getEndTime()
        );
    }
}
