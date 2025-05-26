package com.capstone.backend.member.facade;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Interest;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.service.InterestService;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.TimetableService;
import com.capstone.backend.member.dto.request.CreateInterestRequest;
import com.capstone.backend.member.dto.request.MakeMemberTimetableRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetMemberInfoFacade {
    private final MemberService memberService;
    private final TimetableService timetableService;
    private final InterestService interestService;

    @Transactional
    public Boolean makeTimetable(CustomUserDetails customUserDetails, List<MakeMemberTimetableRequest> makeMemberTimetableRequestList) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        List<Timetable> timetableList = makeMemberTimetableRequestList.stream()
                .map(req -> Timetable.makeTimetable(member.getId(), req))
                .toList();
        timetableService.saveAll(timetableList);
        return true;
    }

    public Boolean createInterestInfo(CustomUserDetails customUserDetails, List<CreateInterestRequest> createInterestInfoRequestList) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        List<Interest> interestList = createInterestInfoRequestList.stream()
                .map(req -> Interest.createInterest(member.getId(), req.interestContent()))
                .toList();
        interestService.saveAll(interestList);
        return true;
    }
}
