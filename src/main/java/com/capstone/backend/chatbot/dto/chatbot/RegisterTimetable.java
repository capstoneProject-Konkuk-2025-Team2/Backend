package com.capstone.backend.chatbot.dto.chatbot;

import com.capstone.backend.member.domain.entity.Timetable;

public record RegisterTimetable(
        String day,
        String startTime,
        String endTime
) {
    public static RegisterTimetable of(
            Timetable timetable
    ) {
        return new RegisterTimetable(
                timetable.getDay().getKorean(),
                timetable.getStringStartTime(),
                timetable.getStringEndTime()
        );
    }
}
