package com.capstone.backend.member.facade;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.ScheduleService;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import com.capstone.backend.member.dto.request.DeleteScheduleRequest;
import com.capstone.backend.member.dto.response.GetScheduleByYearAndMonthResponse;
import com.capstone.backend.member.dto.response.GetScheduleDetailResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {
    private final ScheduleService scheduleService;
    private final MemberService memberService;

    @Transactional
    public Boolean createSchedule(CustomUserDetails customUserDetails, CreateScheduleRequest createScheduleRequest) {
        Long memberId = memberService.getByEmail(customUserDetails.getUsername()).getId();
        scheduleService.save(Schedule.createSchedule(memberId, createScheduleRequest));
        return true;
    }

    @Transactional
    public Boolean changeSchedule(CustomUserDetails customUserDetails, ChangeScheduleRequest changeScheduleRequest) {
        Long memberId = memberService.getByEmail(customUserDetails.getUsername()).getId();
        scheduleService.changeSchedule(memberId, changeScheduleRequest);
        return true;
    }

    @Transactional
    public Boolean deleteSchedule(CustomUserDetails customUserDetails, DeleteScheduleRequest deleteScheduleRequest) {
        Long memberId = memberService.getByEmail(customUserDetails.getUsername()).getId();
        scheduleService.deleteSchedule(memberId, deleteScheduleRequest);
        return true;
    }

    @Transactional(readOnly = true)
    public List<GetScheduleByYearAndMonthResponse> getScheduleByYearAndMonth(Long year, Long month, CustomUserDetails customUserDetails) {
        Long memberId = memberService.getByEmail(customUserDetails.getUsername()).getId();
        return scheduleService.findByMemberIdAndYearAndMonth(memberId, year, month);
    }

    @Transactional(readOnly = true)
    public GetScheduleDetailResponse getScheduleDetail(Long scheduleId, CustomUserDetails customUserDetails) {
        Long memberId = memberService.getByEmail(customUserDetails.getUsername()).getId();
        return scheduleService.getScheduleDetail(memberId, scheduleId);
    }
}
