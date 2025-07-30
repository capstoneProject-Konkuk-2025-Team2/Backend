package com.capstone.backend.member.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.common.support.SpringContextHolder;
import com.capstone.backend.core.common.web.response.exception.ApiError;
import com.capstone.backend.core.configuration.env.AppEnv;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Extracurricular;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ExtraCurricularRepository;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ExtraCurricularRepository extraCurricularRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private AppEnv mockAppEnv;

    @Mock
    private ApplicationContext mockApplicationContext;

    private Schedule schedule;

    private Long memberId;

    @BeforeEach
    void setup() {
        SpringContextHolder.setContextForTesting(mockApplicationContext);
        lenient().when(mockApplicationContext.getBean(AppEnv.class)).thenReturn(mockAppEnv);
        lenient().when(mockAppEnv.getId()).thenReturn("test-env");
        schedule = Schedule.builder()
                .title("스케쥴1")
                .content("세부사항")
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 8, 1))
                .build();
        memberId = 1L;
    }

    @DisplayName("save - 성공")
    @Test
    void save_success() {
        //when
        scheduleService.save(schedule);
        //then
        verify(scheduleRepository).save(schedule);
    }

    @DisplayName("putSchedule - 일반 스케쥴 저장 성공")
    @Test
    void putSchedule_normal_success() {
        //given
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                null
        );
        //when
        scheduleService.putSchedule(memberId, createScheduleRequest);
        //then
        ArgumentCaptor<Schedule> scheduleCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(scheduleCaptor.capture());
        Schedule savedSchedule = scheduleCaptor.getValue();
        assertThat(savedSchedule.getTitle()).isEqualTo(createScheduleRequest.title());
        assertThat(savedSchedule.getContent()).isEqualTo(createScheduleRequest.content());
        assertThat(savedSchedule.getStartDate()).isEqualTo(createScheduleRequest.startDate());
        assertThat(savedSchedule.getEndDate()).isEqualTo(createScheduleRequest.endDate());

        verify(extraCurricularRepository, never()).save(any());
    }

    @DisplayName("putSchedule - 비교과 스케쥴 저장 성공")
    @Test
    void putSchedule_extra_success() {
        //given
        ExtracurricularField extracurricularField = new ExtracurricularField(
                "비교과A",
                "https://abc.cdf",
                LocalDateTime.of(2025,8,1,9,0),
                LocalDateTime.of(2025,8,2,9,0),
                LocalDateTime.of(2025,8,6,9,0),
                LocalDateTime.of(2025,8,6,12,0)
        );
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                extracurricularField
        );
        //when
        scheduleService.putSchedule(memberId, createScheduleRequest);
        ArgumentCaptor<Schedule> scheduleCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(scheduleCaptor.capture());
        Schedule savedSchedule = scheduleCaptor.getValue();
        assertThat(savedSchedule.getTitle()).isEqualTo(createScheduleRequest.title());
        assertThat(savedSchedule.getContent()).isEqualTo(createScheduleRequest.content());
        assertThat(savedSchedule.getStartDate()).isEqualTo(createScheduleRequest.startDate());
        assertThat(savedSchedule.getEndDate()).isEqualTo(createScheduleRequest.endDate());

        ArgumentCaptor<Extracurricular> extraCaptor = ArgumentCaptor.forClass(Extracurricular.class);
        verify(extraCurricularRepository).save(extraCaptor.capture());
        Extracurricular savedExtraCurricular = extraCaptor.getValue();
        assertThat(savedExtraCurricular.getTitle()).isEqualTo(extracurricularField.originTitle());
        assertThat(savedExtraCurricular.getUrl()).isEqualTo(extracurricularField.url());
        assertThat(savedExtraCurricular.getApplicationStart()).isEqualTo(extracurricularField.applicationStart());
        assertThat(savedExtraCurricular.getApplicationEnd()).isEqualTo(extracurricularField.applicationEnd());
        assertThat(savedExtraCurricular.getActivityStart()).isEqualTo(extracurricularField.activityStart());
        assertThat(savedExtraCurricular.getActivityEnd()).isEqualTo(extracurricularField.activityEnd());
    }

    @DisplayName("pusSchedule - 비교과 스케쥴(몇몇 필드는 null) 성공")
    @Test
    void putSchedule_not_perfect_extra_success() {
        //given
        ExtracurricularField extracurricularField = new ExtracurricularField(
                "비교과A",
                null,
                LocalDateTime.of(2025,8,1,9,0),
                null,
                LocalDateTime.of(2025,8,6,9,0),
                LocalDateTime.of(2025,8,6,12,0)
        );
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                extracurricularField
        );
        //when
        scheduleService.putSchedule(memberId, createScheduleRequest);
        ArgumentCaptor<Schedule> scheduleCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(scheduleCaptor.capture());
        Schedule savedSchedule = scheduleCaptor.getValue();
        assertThat(savedSchedule.getTitle()).isEqualTo(createScheduleRequest.title());
        assertThat(savedSchedule.getContent()).isEqualTo(createScheduleRequest.content());
        assertThat(savedSchedule.getStartDate()).isEqualTo(createScheduleRequest.startDate());
        assertThat(savedSchedule.getEndDate()).isEqualTo(createScheduleRequest.endDate());

        ArgumentCaptor<Extracurricular> extraCaptor = ArgumentCaptor.forClass(Extracurricular.class);
        verify(extraCurricularRepository).save(extraCaptor.capture());
        Extracurricular savedExtraCurricular = extraCaptor.getValue();
        assertThat(savedExtraCurricular.getTitle()).isEqualTo(extracurricularField.originTitle());
        assertThat(savedExtraCurricular.getUrl()).isEqualTo(extracurricularField.url());
        assertThat(savedExtraCurricular.getApplicationStart()).isEqualTo(extracurricularField.applicationStart());
        assertThat(savedExtraCurricular.getApplicationEnd()).isEqualTo(extracurricularField.applicationEnd());
        assertThat(savedExtraCurricular.getActivityStart()).isEqualTo(extracurricularField.activityStart());
        assertThat(savedExtraCurricular.getActivityEnd()).isEqualTo(extracurricularField.activityEnd());
    }

    @DisplayName("findByMemberIdAndId - 성공")
    @Test
    void findByMemberIdAndId_success() {
        // given
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        // when
        Optional<Schedule> result = scheduleService.findByMemberIdAndId(memberId, schedule.getId());
        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(schedule);
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
    }

    @DisplayName("getByMemberIdAndId - 성공")
    @Test
    void getByMemberIdAndId_success() {
        // given
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        // when
        Schedule result = scheduleService.getByMemberIdAndId(memberId, schedule.getId());
        //then
        assertThat(result).isEqualTo(schedule);
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
    }

    @DisplayName("getByMemberIdAndId - 해당 객체를 찾지 못할 때")
    @Test
    void getByMemberIdAndId_fail_not_found() {
        // given
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> scheduleService.getByMemberIdAndId(memberId, schedule.getId())
        );

        ApiError error = exception.getError();
        assertThat(error.element().code().value()).isEqualTo("capstone.schedule.not.found");
    }

    @DisplayName("changeSchedule - 성공")
    @Test
    void changeSchedule_success() {
        // given
        ChangeScheduleRequest request = new ChangeScheduleRequest(
                schedule.getId(),
                "변경된 제목",
                "변경된 세부사항",
                ScheduleType.NORMAL,
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 10, 1)
        );

        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));

        // when
        scheduleService.changeSchedule(memberId, request);

        // then
        assertThat(schedule.getTitle()).isEqualTo(request.title());
        assertThat(schedule.getStartDate()).isEqualTo(request.startDate());
        assertThat(schedule.getEndDate()).isEqualTo(request.endDate());
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
    }

    @DisplayName("deleteSchedule - 성공")
    @Test
    void deleteSchedule_success() {
        // given
        DeleteScheduleRequest request = new DeleteScheduleRequest(schedule.getId());
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        // when
        scheduleService.deleteSchedule(memberId, request);
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        verify(scheduleRepository).delete(schedule);
    }

    @DisplayName("findByMemberIdAndYearAndMonth - 성공")
    @Test
    void findByMemberIdAndYearAndMonth_success() {
        //given
        Long year = 2025L;
        Long month = 7L;
        Schedule schedule1 = Schedule.builder()
                .memberId(memberId)
                .title("비교과1")
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 8, 1))
                .build();
        Schedule schedule2 = Schedule.builder()
                .memberId(memberId)
                .title("비교과2")
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 7, 2))
                .build();
        when(scheduleRepository.findByMemberIdAndYearAndMonth(memberId, year, month))
                .thenReturn(List.of(
                        schedule1,
                        schedule2
                ));
        //when
        List<GetScheduleByYearAndMonthResponse> result = scheduleService.findByMemberIdAndYearAndMonth(memberId, year, month);
        //then
        verify(scheduleRepository).findByMemberIdAndYearAndMonth(memberId, year, month);
        assertThat(result).hasSize(2);
        assertThat(result).extracting("scheduleId","title","startDate","endDate").containsExactlyInAnyOrder(
                tuple(schedule1.getId(), schedule1.getTitle(), schedule1.getStartDate(), schedule1.getEndDate()),
                tuple(schedule2.getId(), schedule2.getTitle(), schedule2.getStartDate(), schedule2.getEndDate())
        );
    }

    @DisplayName("getScheduleDetail - 성공")
    @Test
    void getScheduleDetail_success() {
        //given
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        //when
        GetScheduleDetailResponse result = scheduleService.getScheduleDetail(memberId, schedule.getId());
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        assertThat(result)
                .extracting("title", "content", "scheduleType", "startDate", "endDate")
                .containsExactly(
                        schedule.getTitle(),
                        schedule.getContent(),
                        schedule.getStartDate(),
                        schedule.getEndDate()
                );
    }

    @DisplayName("getScheduleDetail - 실패(해당하는 스케쥴을 못찾았을 때)")
    @Test
    void getScheduleDetail_fail_not_found() {
        //given
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.empty());
        //when&then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> scheduleService.getScheduleDetail(memberId, schedule.getId())
        );
        ApiError error = exception.getError();
        assertThat(error.element().code().value()).isEqualTo("capstone.schedule.not.found");
    }
}
