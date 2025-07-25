package com.capstone.backend.member.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Interest;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.service.InterestService;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.TimetableService;
import com.capstone.backend.member.domain.value.AcademicStatus;
import com.capstone.backend.member.domain.value.Day;
import com.capstone.backend.member.domain.value.Role;
import com.capstone.backend.member.dto.response.LookupInterestResponse;
import com.capstone.backend.member.dto.response.LookupMemberInfoResponse;
import com.capstone.backend.member.dto.response.LookupTimetableResponse;
import java.time.LocalTime;
import java.util.Arrays;
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
public class LookupFacadeTest {
    @Mock
    private MemberService memberService;
    @Mock
    private TimetableService timetableService;
    @Mock
    private InterestService interestService;
    @InjectMocks
    private LookupFacade lookupFacade;

    private Member member;
    private CustomUserDetails customUserDetails;
    @BeforeEach
    void setUp() {
        String email = "test@test.com";
        Role role = Role.ROLE_TEMPORARY_MEMBER;
        member = Member.builder()
                .id(1L)
                .email(email)
                .grade(1L)
                .name("김철수")
                .academicStatus(AcademicStatus.ENROLLED)
                .department("컴퓨터공학부")
                .college("공과대학")
                .email("abc@test.com")
                .role(role)
                .build();
        customUserDetails = Mockito.mock(CustomUserDetails.class);
        when(customUserDetails.getUsername()).thenReturn(member.getEmail());
        when(memberService.getByEmail(member.getEmail())).thenReturn(member);

    }

    @DisplayName("시간표 조회")
    @Test
    void lookupTimetable() {
        //given
        when(timetableService.findAllByMemberId(member.getId())).thenReturn(Arrays.asList(
                Timetable.builder()
                        .id(1L)
                        .day(Day.MON)
                        .color("#f6f6f6")
                        .eventName("분산컴퓨팅")
                        .eventDetail("공C487")
                        .startTime(LocalTime.of(9, 0, 0, 0))
                        .endTime(LocalTime.of(11, 0, 0, 0))
                        .memberId(1L)
                        .build(),
                Timetable.builder()
                        .id(2L)
                        .day(Day.TUE)
                        .color("#f6f6f6")
                        .eventName("이산수학")
                        .eventDetail("공C485")
                        .startTime(LocalTime.of(15, 0, 0, 0))
                        .endTime(LocalTime.of(16, 0, 0, 0))
                        .memberId(1L)
                        .build()
        ));
        //when
        List<LookupTimetableResponse> timetableList = lookupFacade.lookupTimetable(customUserDetails);
        //then
        assertThat(timetableList).isNotNull();
        assertThat(timetableList).isNotEmpty();

        assertThat(timetableList)
                .extracting("id", "day", "color", "eventName", "eventDetail", "startTime", "endTime")
                .containsExactly(
                        tuple(1L, Day.MON, "#f6f6f6", "분산컴퓨팅", "공C487", LocalTime.of(9, 0, 0, 0), LocalTime.of(11, 0, 0, 0)),
                        tuple(2L, Day.TUE, "#f6f6f6", "이산수학", "공C485", LocalTime.of(15, 0, 0, 0), LocalTime.of(16, 0, 0, 0))
                );
    }

    @DisplayName("멤버 정보 조회")
    @Test
    void lookupMemberInfo() {
        //when
        LookupMemberInfoResponse lookupMemberInfoResponse = lookupFacade.lookupMemberInfo(customUserDetails);
        //then
        assertEquals(lookupMemberInfoResponse.id(), member.getId());
        assertEquals(lookupMemberInfoResponse.email(), member.getEmail());
        assertEquals(lookupMemberInfoResponse.name(), member.getName());
        assertEquals(lookupMemberInfoResponse.academicStatus(), member.getAcademicStatus());
        assertEquals(lookupMemberInfoResponse.college(), member.getCollege());
        assertEquals(lookupMemberInfoResponse.department(), member.getDepartment());
        assertEquals(lookupMemberInfoResponse.grade(), member.getGrade());
    }

    @DisplayName("관심 사항 조회")
    @Test
    void lookupInterests() {
        //given
        when(interestService.findAllByMemberId(member.getId())).thenReturn(
                List.of(
                        Interest.builder().content("AI").build(),
                        Interest.builder().content("웹").build()
                )
        );
        //when
        LookupInterestResponse lookupInterestResponse = lookupFacade.lookupInterest(customUserDetails);
        //then
        List<String> interests = lookupInterestResponse.interests();
        assertEquals(2, interests.size());
        assertTrue(interests.contains("AI"));
        assertTrue(interests.contains("웹"));
    }
}
