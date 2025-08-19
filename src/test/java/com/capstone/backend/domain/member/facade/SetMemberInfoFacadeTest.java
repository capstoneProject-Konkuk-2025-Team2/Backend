package com.capstone.backend.domain.member.facade;


import static com.capstone.backend.member.domain.value.AcademicStatus.ENROLLED;
import static com.capstone.backend.member.domain.value.Day.MON;
import static com.capstone.backend.member.domain.value.Day.TUE;
import static com.capstone.backend.member.domain.value.Day.WED;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.service.InterestService;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.TimetableService;
import com.capstone.backend.member.domain.value.Role;
import com.capstone.backend.member.dto.request.ChangeTimetableRequest;
import com.capstone.backend.member.dto.request.AcademicInfoRequest;
import com.capstone.backend.member.dto.request.InterestRequest;
import com.capstone.backend.member.dto.request.MakeMemberTimetableRequest;
import com.capstone.backend.member.facade.SetMemberInfoFacade;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SetMemberInfoFacadeTest {
    @Mock
    private MemberService memberService;
    @Mock
    private TimetableService timetableService;
    @Mock
    private InterestService interestService;

    @InjectMocks
    private SetMemberInfoFacade setMemberInfoFacade;

    private Member member;
    private CustomUserDetails customUserDetails;
    @BeforeEach
    void setUp() {
        String email = "test@test.com";
        Role role = Role.ROLE_TEMPORARY_MEMBER;
        member = Member.builder()
                .id(1L)
                .email(email)
                .role(role)
                .build();
        customUserDetails = Mockito.mock(CustomUserDetails.class);
        when(customUserDetails.getUsername()).thenReturn(member.getEmail());
        when(memberService.getByEmail(member.getEmail())).thenReturn(member);
    }

    @DisplayName("시간표 만들기 테스트")
    @Test
    void makeTimetable() {
        //given
        List<MakeMemberTimetableRequest> makeMemberTimetableRequestList = List.of(
                new MakeMemberTimetableRequest(
                        MON,
                        LocalTime.of(11,0,0,0),
                        LocalTime.of(13,0,0,0),
                        "test1",
                        "testDetail1",
                        "#f6f6f6"
                ),
                new MakeMemberTimetableRequest(
                        TUE,
                        LocalTime.of(11,0,0,0),
                        LocalTime.of(13,0,0,0),
                        "test1",
                        "testDetail1",
                        "#f6f6f6"
                ),
                new MakeMemberTimetableRequest(
                        WED,
                        LocalTime.of(11,0,0,0),
                        LocalTime.of(13,0,0,0),
                        "test1",
                        "testDetail1",
                        "#f6f6f6"
                )
        );
        doNothing().when(timetableService).saveAll(anyList());
        //when
        setMemberInfoFacade.makeTimetable(customUserDetails, makeMemberTimetableRequestList);
        //then
        verify(memberService).getByEmail(customUserDetails.getUsername());
        verify(timetableService).saveAll(anyList());
    }

    @DisplayName("관심사항 입력받기 테스트")
    @Test
    void createInterestInfo(){
        //given
        InterestRequest interestRequest = new InterestRequest(
                "AI, 프로그래밍, 경영"
        );
        doNothing().when(interestService).create(member.getId(), interestRequest);
        //when
        setMemberInfoFacade.createInterestInfo(customUserDetails, interestRequest);
        //then
        verify(memberService).getByEmail(customUserDetails.getUsername());
        verify(interestService).create(member.getId(), interestRequest);
    }

    @DisplayName("학적 정보 입력받기 테스트")
    @Test
    void createAcademicInfo() {
        //given
        AcademicInfoRequest academicInfoRequest = new AcademicInfoRequest(
                ENROLLED,
                4L,
                "공과대학",
                "컴퓨터공학부",
                "홍길동"
        );
        doNothing().when(memberService).updateAcademicInfo(member.getId(), academicInfoRequest);
        //when
        setMemberInfoFacade.upsertAcademicInfo(customUserDetails, academicInfoRequest);
        //then
        verify(memberService).getByEmail(customUserDetails.getUsername());
        verify(memberService).updateAcademicInfo(member.getId(), academicInfoRequest);
    }

    @DisplayName("시간표 변경 테스트")
    @Test
    void changeTimetable() {
        //given
        ChangeTimetableRequest changeTimetableRequest = new ChangeTimetableRequest(
                1L,
                TUE,
                LocalTime.of(13,0,0,0),
                LocalTime.of(14,0,0,0),
                "분산시스템",
                "공C487",
                "#f6f6f6"
        );
        Timetable timetable = Timetable.builder().build();
        when(timetableService.findByMemberIdAndId(member.getId(), changeTimetableRequest.id())).thenReturn(timetable);
        //when
        setMemberInfoFacade.changeTimetable(customUserDetails, changeTimetableRequest);
        //then
        verify(timetableService).changeTimetable(timetable, changeTimetableRequest);
    }

    @DisplayName("관심사항 변경 테스트")
    @Test
    void changeInterest() {
        //given
        InterestRequest interestRequest = new InterestRequest("AI, 웹, 경제");
        //when
        setMemberInfoFacade.changeInterest(customUserDetails, interestRequest);
        //then
        verify(memberService).getByEmail(customUserDetails.getUsername());
        verify(interestService).changeInterest(member.getId(), interestRequest);
    }
}
