package com.capstone.backend.member.facade;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.TimetableService;
import com.capstone.backend.member.dto.response.LookupTimetableResponse;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LookupFacade {
    private final MemberService memberService;
    private final TimetableService timetableService;

    @Transactional(readOnly = true)
    public List<LookupTimetableResponse> lookupTimetable(CustomUserDetails customUserDetails) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        List<Timetable> timetableList = timetableService.findAllByMemberId(member.getId());
        return timetableList.stream()
                .sorted(Comparator.comparing(Timetable::getId))
                .map(LookupTimetableResponse::of)
                .toList();
    }
}
