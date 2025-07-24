package com.capstone.backend.member.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.ScheduleService;
import com.capstone.backend.member.domain.value.Role;
import com.capstone.backend.member.domain.value.ScheduleType;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import com.capstone.backend.member.dto.request.DeleteScheduleRequest;
import com.capstone.backend.member.dto.response.GetScheduleByYearAndMonthResponse;
import java.time.LocalDate;
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
public class ScheduleFacadeTest {
    @Mock
    private MemberService memberService;
    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleFacade scheduleFacade;
    private Member member;
    private CustomUserDetails customUserDetails;
    @BeforeEach
    void setUp() {
        String email = "test@test.com";
        Role role = Role.ROLE_MEMBER;
        member = Member.builder()
                .id(1L)
                .email(email)
                .role(role)
                .build();
        customUserDetails = Mockito.mock(CustomUserDetails.class);
        when(customUserDetails.getUsername()).thenReturn(member.getEmail());
        when(memberService.getByEmail(member.getEmail())).thenReturn(member);
    }

    @DisplayName("스케쥴 생성")
    @Test
    void createSchedule() {
        //given
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                "테스트 스케쥴",
                "테스트 상세정보",
                ScheduleType.EXTRACURRICULAR,
                LocalDate.of(2025,7,1),
                LocalDate.of(2025,7,25)
        );
        doNothing().when(scheduleService).save(any());
        //when
        scheduleFacade.createSchedule(customUserDetails, createScheduleRequest);
        //then
        verify(scheduleService).save(any());
    }

    @DisplayName("스케쥴 수정")
    @Test
    void changeSchedule() {
        //given
        ChangeScheduleRequest changeScheduleRequest = new ChangeScheduleRequest(
                1L,
                "스케쥴1",
                "스케쥴 상세정보",
                ScheduleType.EXTRACURRICULAR,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 8, 1)
        );
        doNothing().when(scheduleService).changeSchedule(member.getId(), changeScheduleRequest);
        //when
        Boolean result = scheduleFacade.changeSchedule(customUserDetails, changeScheduleRequest);
        //then
        assertThat(result).isTrue();
        verify(memberService).getByEmail(customUserDetails.getUsername());
        verify(scheduleService).changeSchedule(member.getId(), changeScheduleRequest);
    }

    @DisplayName("스케쥴 삭제")
    @Test
    void deleteSchedule() {
        //given
        DeleteScheduleRequest request = new DeleteScheduleRequest(1L);
        doNothing().when(scheduleService).deleteSchedule(member.getId(), request);
        //when
        Boolean result = scheduleFacade.deleteSchedule(customUserDetails, request);
        //then
        assertThat(result).isTrue();
        verify(memberService).getByEmail(customUserDetails.getUsername());
        verify(scheduleService).deleteSchedule(member.getId(), request);
    }

    @DisplayName("년월별 스케쥴 조회")
    @Test
    void getScheduleByYearAndMonth() {
        //given
        Long year = 2025L;
        Long month = 7L;
        Schedule schedule1 = Schedule.builder()
                .memberId(member.getId())
                .title("비교과1")
                .scheduleType(ScheduleType.EXTRACURRICULAR)
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 8, 1))
                .build();
        Schedule schedule2 = Schedule.builder()
                .memberId(member.getId())
                .title("비교과2")
                .scheduleType(ScheduleType.NORMAL)
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 7, 2))
                .build();
        GetScheduleByYearAndMonthResponse response1 = GetScheduleByYearAndMonthResponse.of(schedule1);
        GetScheduleByYearAndMonthResponse response2 = GetScheduleByYearAndMonthResponse.of(schedule2);
        when(scheduleService.findByMemberIdAndYearAndMonth(member.getId(), year, month))
                .thenReturn(List.of(response1, response2));
        //when
        List<GetScheduleByYearAndMonthResponse> result = scheduleFacade.getScheduleByYearAndMonth(year, month, customUserDetails);
        //then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("scheduleId","title","scheduleType","startDate","endDate").containsExactlyInAnyOrder(
                tuple(response1.scheduleId(), response1.title(), response1.scheduleType(), response1.startDate(), response1.endDate()),
                tuple(response2.scheduleId(), response2.title(), response2.scheduleType(), response2.startDate(), response2.endDate())
        );
    }
}
