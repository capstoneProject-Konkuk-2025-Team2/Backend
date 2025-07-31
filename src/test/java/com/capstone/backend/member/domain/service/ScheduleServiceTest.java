package com.capstone.backend.member.domain.service;

import static com.capstone.backend.member.domain.value.ScheduleType.EXTRACURRICULAR;
import static com.capstone.backend.member.domain.value.ScheduleType.NORMAL;
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
import com.capstone.backend.member.domain.repository.ScheduleRepository;
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
    private ExtracurricularService extraCurricularService;

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private AppEnv mockAppEnv;

    @Mock
    private ApplicationContext mockApplicationContext;

    private Schedule schedule;

    private Extracurricular extracurricular;

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
        extracurricular = Extracurricular.builder()
                .id(1L)
                .title("비교과A")
                .url("https://abc.cdf")
                .applicationStart(LocalDateTime.of(2025,8,1,9,0))
                .applicationEnd(LocalDateTime.of(2025,8,2,9,0))
                .activityStart(LocalDateTime.of(2025,8,6,9,0))
                .activityEnd(LocalDateTime.of(2025,8,6,12,0))
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

        verify(extraCurricularService, never()).createExtracurricular(any());
    }

    @DisplayName("putSchedule - 비교과 스케쥴 저장 성공")
    @Test
    void putSchedule_extra_success() {
        //given
        ExtracurricularField extracurricularField = new ExtracurricularField(
                extracurricular.getTitle(),
                extracurricular.getUrl(),
                extracurricular.getApplicationStart(),
                extracurricular.getApplicationEnd(),
                extracurricular.getActivityStart(),
                extracurricular.getActivityEnd()
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

        ArgumentCaptor<ExtracurricularField> extraCaptor = ArgumentCaptor.forClass(ExtracurricularField.class);
        verify(extraCurricularService).createExtracurricular(extraCaptor.capture());
        ExtracurricularField savedExtraCurricularField = extraCaptor.getValue();
        assertThat(savedExtraCurricularField.originTitle()).isEqualTo(extracurricularField.originTitle());
        assertThat(savedExtraCurricularField.url()).isEqualTo(extracurricularField.url());
        assertThat(savedExtraCurricularField.applicationStart()).isEqualTo(extracurricularField.applicationStart());
        assertThat(savedExtraCurricularField.applicationEnd()).isEqualTo(extracurricularField.applicationEnd());
        assertThat(savedExtraCurricularField.activityStart()).isEqualTo(extracurricularField.activityStart());
        assertThat(savedExtraCurricularField.activityEnd()).isEqualTo(extracurricularField.activityEnd());
    }

    @DisplayName("putSchedule - 비교과 스케쥴(몇몇 필드는 null) 성공")
    @Test
    void putSchedule_not_perfect_extra_success() {
        //given
        ExtracurricularField extracurricularField = new ExtracurricularField(
                extracurricular.getTitle(),
                null,
                extracurricular.getApplicationStart(),
                extracurricular.getApplicationEnd(),
                null,
                extracurricular.getActivityEnd()
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

        ArgumentCaptor<ExtracurricularField> extraCaptor = ArgumentCaptor.forClass(ExtracurricularField.class);
        verify(extraCurricularService).createExtracurricular(extraCaptor.capture());
        ExtracurricularField savedExtraCurricularField = extraCaptor.getValue();
        assertThat(savedExtraCurricularField.originTitle()).isEqualTo(extracurricularField.originTitle());
        assertThat(savedExtraCurricularField.url()).isEqualTo(extracurricularField.url());
        assertThat(savedExtraCurricularField.applicationStart()).isEqualTo(extracurricularField.applicationStart());
        assertThat(savedExtraCurricularField.applicationEnd()).isEqualTo(extracurricularField.applicationEnd());
        assertThat(savedExtraCurricularField.activityStart()).isEqualTo(extracurricularField.activityStart());
        assertThat(savedExtraCurricularField.activityEnd()).isEqualTo(extracurricularField.activityEnd());
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

    @DisplayName("changeSchedule - (비교과 일정 아님) -> (비교과 일정 아님)")
    @Test
    void changeSchedule_notExtra_to_notExtra_success() {
        // given
        ChangeScheduleRequest request = new ChangeScheduleRequest(
                schedule.getId(),
                "변경된 제목",
                "변경된 세부사항",
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 10, 1),
                null
        );

        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));

        // when
        scheduleService.changeSchedule(memberId, request);

        // then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        assertThat(schedule.getId()).isEqualTo(request.scheduleId());
        assertThat(schedule.getTitle()).isEqualTo(request.title());
        assertThat(schedule.getStartDate()).isEqualTo(request.startDate());
        assertThat(schedule.getEndDate()).isEqualTo(request.endDate());
        assertThat(schedule.getExtracurricularId()).isNull();
    }

    @DisplayName("changeSchedule - (비교과 일정 아님) -> (비교과 일정)")
    @Test
    void changeSchedule_notExtra_to_extra_success() {
        // given
        ExtracurricularField extracurricularField = new ExtracurricularField(
                extracurricular.getTitle(),
                extracurricular.getUrl(),
                extracurricular.getApplicationStart(),
                extracurricular.getApplicationEnd(),
                extracurricular.getActivityStart(),
                extracurricular.getActivityEnd()
        );
        ChangeScheduleRequest request = new ChangeScheduleRequest(
                schedule.getId(),
                "변경된 제목",
                "변경된 세부사항",
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 10, 1),
                extracurricularField
        );
        Extracurricular newExtra = Extracurricular.builder()
                        .id(1L)
                        .build();
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        when(extraCurricularService.createExtracurricular(request.extracurricularField())).thenReturn(newExtra);
        //when
        scheduleService.changeSchedule(memberId, request);
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        assertThat(schedule.getId()).isEqualTo(request.scheduleId());
        assertThat(schedule.getTitle()).isEqualTo(request.title());
        assertThat(schedule.getStartDate()).isEqualTo(request.startDate());
        assertThat(schedule.getEndDate()).isEqualTo(request.endDate());
        verify(extraCurricularService).createExtracurricular(request.extracurricularField());
        assertThat(schedule.getExtracurricularId()).isEqualTo(newExtra.getId());
    }

    @DisplayName("changeSchedule - (비교과 일정) -> (비교과 일정 아님)")
    @Test
    void changeSchedule_extra_to_notExtra_success() {
        // given
        ChangeScheduleRequest request = new ChangeScheduleRequest(
                schedule.getId(),
                "변경된 제목",
                "변경된 세부사항",
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 10, 1),
                null
        );
        Extracurricular newExtra = Extracurricular.builder()
                .id(1L)
                .build();
        schedule.connectExtracurricular(newExtra.getId());
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        //when
        scheduleService.changeSchedule(memberId, request);
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        assertThat(schedule.getId()).isEqualTo(request.scheduleId());
        assertThat(schedule.getTitle()).isEqualTo(request.title());
        assertThat(schedule.getStartDate()).isEqualTo(request.startDate());
        assertThat(schedule.getEndDate()).isEqualTo(request.endDate());
        assertThat(schedule.getExtracurricularId()).isNull();
        verify(extraCurricularService).deleteExtracurricular(newExtra.getId());
    }

    @DisplayName("changeSchedule - (비교과 일정) -> (비교과 일정)")
    @Test
    void changeSchedule_extra_to_extra_success() {
        // given
        ExtracurricularField extracurricularField = new ExtracurricularField(
                "비교과A",
                null,
                LocalDateTime.of(2025,8,1,9,0),
                null,
                LocalDateTime.of(2025,8,6,9,0),
                LocalDateTime.of(2025,8,6,12,0)
        );
        ChangeScheduleRequest request = new ChangeScheduleRequest(
                schedule.getId(),
                "변경된 제목",
                "변경된 세부사항",
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 10, 1),
                extracurricularField
        );
        Extracurricular newExtra = Extracurricular.builder()
                .id(1L)
                .build();
        schedule.connectExtracurricular(newExtra.getId());
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        //when
        scheduleService.changeSchedule(memberId, request);
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        assertThat(schedule.getId()).isEqualTo(request.scheduleId());
        assertThat(schedule.getTitle()).isEqualTo(request.title());
        assertThat(schedule.getStartDate()).isEqualTo(request.startDate());
        assertThat(schedule.getEndDate()).isEqualTo(request.endDate());
        verify(extraCurricularService).changeExtracurricular(newExtra.getId(), request.extracurricularField());
    }

    @DisplayName("deleteSchedule - 성공(일반 일정 일 때)")
    @Test
    void deleteSchedule_success_normal() {
        // given
        DeleteScheduleRequest request = new DeleteScheduleRequest(schedule.getId());
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        // when
        scheduleService.deleteSchedule(memberId, request);
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        verify(scheduleRepository).delete(schedule);
        verify(extraCurricularService, never()).deleteExtracurricular(schedule.getExtracurricularId());
    }

    @DisplayName("deleteSchedule - 성공(비교과 관련 일정 일 떄)")
    @Test
    void deleteSchedule_success_extra() {
        // given
        schedule.connectExtracurricular(extracurricular.getId());
        DeleteScheduleRequest request = new DeleteScheduleRequest(schedule.getId());
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        // when
        scheduleService.deleteSchedule(memberId, request);
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        verify(scheduleRepository).delete(schedule);
        verify(extraCurricularService).deleteExtracurricular(schedule.getExtracurricularId());
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

    @DisplayName("getScheduleDetail - 성공(비교과 관련)")
    @Test
    void getScheduleDetail_extra_success() {
        //given
        schedule.connectExtracurricular(extracurricular.getId());
        ExtracurricularField expectedField = new ExtracurricularField(
                extracurricular.getTitle(),
                extracurricular.getUrl(),
                extracurricular.getApplicationStart(),
                extracurricular.getApplicationEnd(),
                extracurricular.getActivityStart(),
                extracurricular.getActivityEnd()
        );
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        when(extraCurricularService.getById(extracurricular.getId()))
                .thenReturn(extracurricular);
        //when
        GetScheduleDetailResponse result = scheduleService.getScheduleDetail(memberId, schedule.getId());
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        verify(extraCurricularService).getById(extracurricular.getId());
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

    @DisplayName("getScheduleDetail - 성공(일반 일정)")
    @Test
    void getScheduleDetail_normal_success() {
        //given
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        //when
        GetScheduleDetailResponse result = scheduleService.getScheduleDetail(memberId, schedule.getId());
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
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
