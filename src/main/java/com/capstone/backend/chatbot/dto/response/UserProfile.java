package com.capstone.backend.chatbot.dto.response;

import com.capstone.backend.chatbot.dto.RegisterTimetable;
import java.util.List;

public record UserProfile(
        Long id,
        String name,
        String major,
        String year,
        List<String> interests,
        List<RegisterTimetable> timetable
) {

}
