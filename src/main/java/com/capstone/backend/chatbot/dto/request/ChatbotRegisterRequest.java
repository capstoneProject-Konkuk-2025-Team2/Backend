package com.capstone.backend.chatbot.dto.request;

import com.capstone.backend.chatbot.dto.RegisterTimetable;
import com.capstone.backend.member.domain.entity.Member;
import java.util.List;

public record ChatbotRegisterRequest (
        Long id,
        String name,
        String major,
        String grade,
        List<String> interests,
        List<RegisterTimetable> timetable
) {
    public static ChatbotRegisterRequest of(
            Member member,
            List<String> interest,
            List<RegisterTimetable> timetable
    ) {
        return new ChatbotRegisterRequest(
                member.getId(),
                member.getName(),
                member.getDepartment(),
                member.getStringGrade(),
                interest,
                timetable
        );
    }
}
