package com.capstone.backend.member.facade;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.ScheduleService;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {
    private final ScheduleService scheduleService;
    private final MemberService memberService;
    public Boolean createSchedule(CustomUserDetails customUserDetails, CreateScheduleRequest createScheduleRequest) {
        Long memberId = memberService.getByEmail(customUserDetails.getUsername()).getId();
        scheduleService.save(Schedule.createSchedule(memberId, createScheduleRequest));
        return true;
    }

    public Boolean changeSchedule(CustomUserDetails customUserDetails, ChangeScheduleRequest changeScheduleRequest) {
        Long memberId = memberService.getByEmail(customUserDetails.getUsername()).getId();
        scheduleService.changeSchedule(memberId, changeScheduleRequest);
        return true;
    }
}
