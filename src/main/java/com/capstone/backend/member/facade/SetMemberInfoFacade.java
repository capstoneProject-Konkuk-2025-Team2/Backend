package com.capstone.backend.member.facade;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.service.InterestService;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.TimetableService;
import com.capstone.backend.member.dto.request.ChangeTimetableRequest;
import com.capstone.backend.member.dto.request.AcademicInfoRequest;
import com.capstone.backend.member.dto.request.DeleteTimetableRequest;
import com.capstone.backend.member.dto.request.InterestRequest;
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

    @Transactional
    public Boolean createInterestInfo(CustomUserDetails customUserDetails, InterestRequest interestRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        interestService.create(member.getId(), interestRequest);
        return true;
    }

    @Transactional
    public Boolean upsertAcademicInfo(CustomUserDetails customUserDetails, AcademicInfoRequest academicInfoRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        memberService.updateAcademicInfo(member.getId(), academicInfoRequest);
        return true;
    }

    @Transactional
    public Boolean changeTimetable(CustomUserDetails customUserDetails, ChangeTimetableRequest changeTimetableRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        Timetable timetable = timetableService.findByMemberIdAndId(member.getId(), changeTimetableRequest.id());
        timetableService.changeTimetable(timetable, changeTimetableRequest);
        return true;
    }

    @Transactional
    public Boolean changeInterest(CustomUserDetails customUserDetails, InterestRequest interestRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        interestService.changeInterest(member.getId(), interestRequest);
        return true;
    }

    @Transactional
    public Boolean deleteTimetable(CustomUserDetails customUserDetails, DeleteTimetableRequest deleteTimetableRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        timetableService.findByMemberIdAndId(member.getId(), deleteTimetableRequest.deleteTimetableId());
        timetableService.deleteMemberIdAndTimetableId(member.getId(), deleteTimetableRequest.deleteTimetableId());
        return true;
    }
}
