package com.capstone.backend.member.facade;

import static com.capstone.backend.member.domain.value.ScheduleType.EXTRACURRICULAR;
import static com.capstone.backend.member.domain.value.ScheduleType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Extracurricular;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.ScheduleService;
import com.capstone.backend.member.domain.value.Role;
import com.capstone.backend.member.domain.value.ScheduleType;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import com.capstone.backend.member.dto.request.DeleteScheduleRequest;
import com.capstone.backend.member.dto.request.ExtracurricularField;
import com.capstone.backend.member.dto.response.GetScheduleByYearAndMonthResponse;
import com.capstone.backend.member.dto.response.GetScheduleDetailResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @DisplayName("스케쥴 생성 - 일반 일정")
    @Test
    void createSchedule_normal() {
        //given
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                "테스트 스케쥴",
                "테스트 상세정보",
                LocalDate.of(2025,7,1),
                LocalDate.of(2025,7,25),
                null
        );
        //when
        scheduleFacade.createSchedule(customUserDetails, createScheduleRequest);
        //then
        verify(scheduleService).putSchedule(member.getId(), createScheduleRequest);
    }

    @DisplayName("스케쥴 생성 - 비교과 일정")
    @Test
    void createSchedule_extra() {
        //given
        ExtracurricularField extracurricularField = new ExtracurricularField(
                "비교과A",
                "https://abc.def",
                LocalDateTime.of(2025,8,1,9,0),
                LocalDateTime.of(2025,8,2,9,0),
                LocalDateTime.of(2025,8,6,9,0),
                LocalDateTime.of(2025,8,6,12,0)
        );
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                "테스트 스케쥴",
                "테스트 상세정보",
                LocalDate.of(2025,7,1),
                LocalDate.of(2025,7,25),
                extracurricularField
        );
        //when
        scheduleFacade.createSchedule(customUserDetails, createScheduleRequest);
        //then
        verify(scheduleService).putSchedule(member.getId(), createScheduleRequest);
    }

    @DisplayName("스케쥴 수정")
    @Test
    void changeSchedule() {
        //given
        ExtracurricularField extracurricularField = new ExtracurricularField(
                "비교과A",
                "https://abc.def",
                LocalDateTime.of(2025,8,1,9,0),
                LocalDateTime.of(2025,8,2,9,0),
                LocalDateTime.of(2025,8,6,9,0),
                LocalDateTime.of(2025,8,6,12,0)
        );
        ChangeScheduleRequest changeScheduleRequest = new ChangeScheduleRequest(
                1L,
                "스케쥴1",
                "스케쥴 상세정보",
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 8, 1),
                extracurricularField
        );
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
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 8, 1))
                .build();
        Schedule schedule2 = Schedule.builder()
                .memberId(member.getId())
                .title("비교과2")
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

    @DisplayName("스케쥴 상세 조회 - 비교과 관련 일정인 경우")
    @Test
    void getScheduleDetail_extra() {
        //given
        Extracurricular extracurricular = Extracurricular.builder()
                .title("비교과A")
                .url("https://abc.cdf")
                .applicationStart(LocalDateTime.of(2025,8,1,9,0))
                .applicationEnd(LocalDateTime.of(2025,8,2,9,0))
                .activityStart(LocalDateTime.of(2025,8,6,9,0))
                .activityEnd(LocalDateTime.of(2025,8,6,12,0))
                .build();
        Schedule schedule = Schedule.builder()
                .memberId(member.getId())
                .title("비교과1")
                .content("비교과 상세정보")
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 8, 1))
                .extracurricularId(extracurricular.getId())
                .build();
        GetScheduleDetailResponse response = GetScheduleDetailResponse.of(schedule, extracurricular);
        ExtracurricularField expectedField = new ExtracurricularField(
                extracurricular.getTitle(),
                extracurricular.getUrl(),
                extracurricular.getApplicationStart(),
                extracurricular.getApplicationEnd(),
                extracurricular.getActivityStart(),
                extracurricular.getActivityEnd()
        );
        when(scheduleService.getScheduleDetail(member.getId(), schedule.getId()))
                .thenReturn(response);
        //when
        GetScheduleDetailResponse result = scheduleFacade.getScheduleDetail(schedule.getId(), customUserDetails);
        //then
        verify(scheduleService).getScheduleDetail(member.getId(), schedule.getId());
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new GetScheduleDetailResponse(
                        schedule.getTitle(),
                        schedule.getContent(),
                        EXTRACURRICULAR,
                        schedule.getStartDate(),
                        schedule.getEndDate(),
                        expectedField
                ));
    }

    @DisplayName("스케쥴 상세 조회 - 일반 일정인 경우")
    @Test
    void getScheduleDetail_normal() {
        //given
        Schedule schedule = Schedule.builder()
                .memberId(member.getId())
                .title("비교과1")
                .content("비교과 상세정보")
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 8, 1))
                .build();
        GetScheduleDetailResponse response = GetScheduleDetailResponse.of(schedule, null);
        when(scheduleService.getScheduleDetail(member.getId(), schedule.getId()))
                .thenReturn(response);
        //when
        GetScheduleDetailResponse result = scheduleFacade.getScheduleDetail(schedule.getId(), customUserDetails);
        //then
        verify(scheduleService).getScheduleDetail(member.getId(), schedule.getId());
        assertThat(result)
                .extracting("title", "content", "scheduleType", "startDate", "endDate", "extracurricularField")
                .containsExactly(
                        schedule.getTitle(),
                        schedule.getContent(),
                        NORMAL,
                        schedule.getStartDate(),
                        schedule.getEndDate(),
                        null
                );
    }
}
