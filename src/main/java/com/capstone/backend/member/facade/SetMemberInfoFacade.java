package com.capstone.backend.member.facade;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Interest;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.service.InterestService;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.TimetableService;
import com.capstone.backend.member.dto.request.ChangeTimetableRequest;
import com.capstone.backend.member.dto.request.CreateAcademicInfoRequest;
import com.capstone.backend.member.dto.request.MakeMemberTimetableRequest;
import java.util.Arrays;
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

    @Transactional
    public Boolean createInterestInfo(CustomUserDetails customUserDetails, String createInterestInfoRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        List<Interest> interestList = Arrays.stream(createInterestInfoRequest.split(","))
                .map(String::trim)
                .filter(interest -> !interest.isEmpty())
                .distinct()
                .map(req -> Interest.createInterest(member.getId(), req))
                .toList();
        interestService.saveAll(interestList);
        return true;
    }

    @Transactional
    public Boolean createAcademicInfo(CustomUserDetails customUserDetails, CreateAcademicInfoRequest createAcademicInfoRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        memberService.updateAcademicInfo(member.getId(), createAcademicInfoRequest);
        return true;
    }

    @Transactional
    public Boolean changeTimetable(CustomUserDetails customUserDetails, ChangeTimetableRequest changeTimetableRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        Timetable timetable = timetableService.findByMemberIdAndId(member.getId(), changeTimetableRequest.id());
        timetableService.changeTimetable(timetable, changeTimetableRequest);
        return true;
    }
}
