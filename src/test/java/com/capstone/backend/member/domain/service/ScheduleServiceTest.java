package com.capstone.backend.member.domain.service;

import static com.capstone.backend.member.domain.value.ScheduleType.EXTRACURRICULAR;
import static com.capstone.backend.member.domain.value.ScheduleType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.common.support.SpringContextHolder;
import com.capstone.backend.core.common.web.response.exception.ApiError;
import com.capstone.backend.core.configuration.env.AppEnv;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.extracurricular.domain.service.ExtracurricularService;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import com.capstone.backend.member.dto.request.DeleteScheduleRequest;
import com.capstone.backend.member.dto.request.ExtracurricularField;
import com.capstone.backend.member.dto.response.GetScheduleByYearAndMonthResponse;
import com.capstone.backend.member.dto.response.GetScheduleDetailResponse;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ExtracurricularService extracurricularService;

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
                .startDateTime(LocalDateTime.of(2025, 7, 1,0,0,0))
                .endDateTime(LocalDateTime.of(2025, 8, 1,0,0,0))
                .build();
        extracurricular = Extracurricular.builder()
                .id(1L)
                .extracurricularId(1L)
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
                schedule.getStartDateTime(),
                schedule.getEndDateTime(),
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
        assertThat(savedSchedule.getStartDateTime()).isEqualTo(createScheduleRequest.startDateTime());
        assertThat(savedSchedule.getEndDateTime()).isEqualTo(createScheduleRequest.endDateTime());
        assertThat(savedSchedule.getExtracurricularId()).isEqualTo(createScheduleRequest.extracurricularId());
    }

    @DisplayName("putSchedule - 비교과 스케쥴 저장 성공")
    @Test
    void putSchedule_extra_success() {
        //given
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                schedule.getTitle(),
                schedule.getContent(),
                null,
                null,
                extracurricular.getExtracurricularId()
        );
        when(extracurricularService.getByExtracurricularId(createScheduleRequest.extracurricularId())).thenReturn(extracurricular);
        //when
        scheduleService.putSchedule(memberId, createScheduleRequest);
        //then
        ArgumentCaptor<Schedule> scheduleCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(scheduleCaptor.capture());
        Schedule savedSchedule = scheduleCaptor.getValue();
        assertThat(savedSchedule.getTitle()).isEqualTo(createScheduleRequest.title());
        assertThat(savedSchedule.getContent()).isEqualTo(createScheduleRequest.content());
        assertThat(savedSchedule.getStartDateTime()).isEqualTo(extracurricular.getActivityStart());
        assertThat(savedSchedule.getEndDateTime()).isEqualTo(extracurricular.getActivityEnd());
        assertThat(savedSchedule.getExtracurricularId()).isEqualTo(createScheduleRequest.extracurricularId());
    }

    @DisplayName("putSchedule - 존재하지 않는 비교과 저장시 오류 발생")
    @Test
    void putSchedule_fail_extra_not_found() {
        //given
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                schedule.getTitle(),
                schedule.getContent(),
                null,
                null,
                3L
        );
        doThrow(new CustomException("capstone.extra.not.found"))
                .when(extracurricularService)
                .getByExtracurricularId(createScheduleRequest.extracurricularId());
        //when & then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> scheduleService.putSchedule(memberId, createScheduleRequest)
        );
        ApiError error = exception.getError();
        assertThat(error.element().code().value()).isEqualTo("capstone.extra.not.found");
    }

    @DisplayName("putSchedule - 비교과 활동기간이 null일때 비교과 신청기간이 스케쥴 date에 들어가야함")
    @Test
    void putSchedule_extra_activity_date_null() {
        //given
        Extracurricular extracurricular_activity_null = Extracurricular.builder()
                .id(1L)
                .extracurricularId(2L)
                .title("비교과A")
                .url("https://abc.cdf")
                .applicationStart(LocalDateTime.of(2025,8,1,9,0))
                .applicationEnd(LocalDateTime.of(2025,8,2,9,0))
                .activityStart(null)
                .activityEnd(null)
                .build();
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                schedule.getTitle(),
                schedule.getContent(),
                null,
                null,
                extracurricular_activity_null.getExtracurricularId()
        );
        when(extracurricularService.getByExtracurricularId(createScheduleRequest.extracurricularId())).thenReturn(extracurricular_activity_null);
        //when
        scheduleService.putSchedule(memberId, createScheduleRequest);
        ArgumentCaptor<Schedule> scheduleCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(scheduleCaptor.capture());
        Schedule savedSchedule = scheduleCaptor.getValue();
        assertThat(savedSchedule.getTitle()).isEqualTo(createScheduleRequest.title());
        assertThat(savedSchedule.getContent()).isEqualTo(createScheduleRequest.content());
        assertThat(savedSchedule.getStartDateTime()).isEqualTo(extracurricular_activity_null.getApplicationStart());
        assertThat(savedSchedule.getEndDateTime()).isEqualTo(extracurricular_activity_null.getApplicationEnd());
        assertThat(savedSchedule.getExtracurricularId()).isEqualTo(createScheduleRequest.extracurricularId());
    }

    @DisplayName("putSchedule - 비교과 신청기간이 null일때 비교과 오늘날짜가 스케쥴 date에 들어가야함")
    @Test
    void putSchedule_extra_application_date_null() {
        //given
        Extracurricular extracurricular_activity_null = Extracurricular.builder()
                .id(1L)
                .extracurricularId(2L)
                .title("비교과A")
                .url("https://abc.cdf")
                .applicationStart(null)
                .applicationEnd(null)
                .activityStart(null)
                .activityEnd(null)
                .build();
        CreateScheduleRequest createScheduleRequest = new CreateScheduleRequest(
                schedule.getTitle(),
                schedule.getContent(),
                null,
                null,
                extracurricular_activity_null.getExtracurricularId()
        );
        LocalDateTime startDateTimeStub = LocalDateTime.now();
        LocalDateTime endDateTimeStub = LocalDateTime.now();
        when(extracurricularService.getByExtracurricularId(createScheduleRequest.extracurricularId())).thenReturn(extracurricular_activity_null);
        //when
        scheduleService.putSchedule(memberId, createScheduleRequest);
        ArgumentCaptor<Schedule> scheduleCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(scheduleCaptor.capture());
        Schedule savedSchedule = scheduleCaptor.getValue();
        assertThat(savedSchedule.getTitle()).isEqualTo(createScheduleRequest.title());
        assertThat(savedSchedule.getContent()).isEqualTo(createScheduleRequest.content());
        assertThat(savedSchedule.getStartDateTime()).isBetween(startDateTimeStub.minusSeconds(1), startDateTimeStub.plusSeconds(1));
        assertThat(savedSchedule.getEndDateTime()).isBetween(endDateTimeStub.minusSeconds(1), endDateTimeStub.plusSeconds(1));
        assertThat(savedSchedule.getExtracurricularId()).isEqualTo(createScheduleRequest.extracurricularId());
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
                LocalDateTime.of(2025, 9, 1,0,0,0),
                LocalDateTime.of(2025, 10, 1,0,0,0),
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
        assertThat(schedule.getStartDateTime()).isEqualTo(request.startDateTime());
        assertThat(schedule.getEndDateTime()).isEqualTo(request.endDateTime());
        assertThat(schedule.getExtracurricularId()).isNull();
    }

    @DisplayName("changeSchedule - (비교과 일정 아님) -> (비교과 일정)")
    @Test
    void changeSchedule_notExtra_to_extra_success() {
        // given
        ChangeScheduleRequest request = new ChangeScheduleRequest(
                schedule.getId(),
                "변경된 제목",
                "변경된 세부사항",
                LocalDateTime.of(2025, 9, 1,0,0,0),
                LocalDateTime.of(2025, 10, 1,0,0,0),
                3L
        );
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, schedule.getId()))
                .thenReturn(Optional.of(schedule));
        //when
        scheduleService.changeSchedule(memberId, request);
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
        assertThat(schedule.getId()).isEqualTo(request.scheduleId());
        assertThat(schedule.getTitle()).isEqualTo(request.title());
        assertThat(schedule.getStartDateTime()).isEqualTo(request.startDateTime());
        assertThat(schedule.getEndDateTime()).isEqualTo(request.endDateTime());
        assertThat(schedule.getExtracurricularId()).isEqualTo(request.extracurricularId());
    }

    @DisplayName("changeSchedule - (비교과 일정) -> (비교과 일정 아님)")
    @Test
    void changeSchedule_extra_to_notExtra_success() {
        // given
        Schedule extraSchedule = Schedule.builder()
                .title("스케쥴1")
                .content("세부사항")
                .startDateTime(LocalDateTime.of(2025, 7, 1,0,0,0))
                .endDateTime(LocalDateTime.of(2025, 8, 1,0,0,0))
                .extracurricularId(3L)
                .build();
        ChangeScheduleRequest request = new ChangeScheduleRequest(
                schedule.getId(),
                "변경된 제목",
                "변경된 세부사항",
                LocalDateTime.of(2025, 9, 1,0,0,0),
                LocalDateTime.of(2025, 10, 1,0,0,0),
                null
        );
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, extraSchedule.getId()))
                .thenReturn(Optional.of(extraSchedule));
        //when
        scheduleService.changeSchedule(memberId, request);
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, extraSchedule.getId());
        assertThat(extraSchedule.getId()).isEqualTo(request.scheduleId());
        assertThat(extraSchedule.getTitle()).isEqualTo(request.title());
        assertThat(extraSchedule.getStartDateTime()).isEqualTo(request.startDateTime());
        assertThat(extraSchedule.getEndDateTime()).isEqualTo(request.endDateTime());
        assertThat(extraSchedule.getExtracurricularId()).isNull();
    }

    @DisplayName("changeSchedule - (비교과 일정) -> (비교과 일정)")
    @Test
    void changeSchedule_extra_to_extra_success() {
        // given
        Schedule extraSchedule = Schedule.builder()
                .title("스케쥴1")
                .content("세부사항")
                .startDateTime(LocalDateTime.of(2025, 7, 1,0,0,0))
                .endDateTime(LocalDateTime.of(2025, 8, 1,0,0,0))
                .extracurricularId(3L)
                .build();
        ChangeScheduleRequest request = new ChangeScheduleRequest(
                schedule.getId(),
                "변경된 제목",
                "변경된 세부사항",
                LocalDateTime.of(2025, 9, 1,0,0,0),
                LocalDateTime.of(2025, 10, 1,0,0,0),
                extracurricular.getExtracurricularId()
        );
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, extraSchedule.getId()))
                .thenReturn(Optional.of(extraSchedule));
        //when
        scheduleService.changeSchedule(memberId, request);
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, extraSchedule.getId());
        assertThat(extraSchedule.getId()).isEqualTo(request.scheduleId());
        assertThat(extraSchedule.getTitle()).isEqualTo(request.title());
        assertThat(extraSchedule.getStartDateTime()).isEqualTo(request.startDateTime());
        assertThat(extraSchedule.getEndDateTime()).isEqualTo(request.endDateTime());
        assertThat(extraSchedule.getExtracurricularId()).isEqualTo(request.extracurricularId());
    }

    @DisplayName("changeSchedule - 변경할 비교과가 존재하지 않을 때 오류 발생")
    @Test
    void changeSchedule_fail_not_found_extra() {
        //given
        ChangeScheduleRequest request = new ChangeScheduleRequest(
                schedule.getId(),
                "변경된 제목",
                "변경된 세부사항",
                null,
                null,
                3L
        );
        doThrow(new CustomException("capstone.extra.not.found"))
                .when(extracurricularService)
                .getByExtracurricularId(request.extracurricularId());
        //when & then
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, request.scheduleId())).thenReturn(Optional.of(schedule));
        CustomException exception = assertThrows(
                CustomException.class,
                () -> scheduleService.changeSchedule(memberId, request)
        );
        ApiError error = exception.getError();
        assertThat(error.element().code().value()).isEqualTo("capstone.extra.not.found");
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
                .startDateTime(LocalDateTime.of(2025, 7, 1,0,0,0))
                .endDateTime(LocalDateTime.of(2025, 8, 1,0,0,0))
                .build();
        Schedule schedule2 = Schedule.builder()
                .memberId(memberId)
                .title("비교과2")
                .startDateTime(LocalDateTime.of(2025, 6, 1,0,0,0))
                .endDateTime(LocalDateTime.of(2025, 7, 2,0,0,0))
                .build();
        YearMonth ym = YearMonth.of(year.intValue(), month.intValue());
        LocalDateTime startInclusive = ym.atDay(1).atStartOfDay();
        LocalDateTime endExclusive = ym.plusMonths(1).atDay(1).atStartOfDay();
        when(scheduleRepository.findByMemberIdAndOverlappingRange(memberId, startInclusive, endExclusive))
                .thenReturn(List.of(
                        schedule1,
                        schedule2
                ));
        //when
        List<GetScheduleByYearAndMonthResponse> result = scheduleService.findByMemberIdAndYearAndMonth(memberId, year, month);
        //then
        verify(scheduleRepository).findByMemberIdAndOverlappingRange(memberId, startInclusive, endExclusive);
        assertThat(result).hasSize(2);
        assertThat(result).extracting("scheduleId","title","startDateTime","endDateTime").containsExactlyInAnyOrder(
                tuple(schedule1.getId(), schedule1.getTitle(), schedule1.getStartDateTime(), schedule1.getEndDateTime()),
                tuple(schedule2.getId(), schedule2.getTitle(), schedule2.getStartDateTime(), schedule2.getEndDateTime())
        );
    }

    @DisplayName("getScheduleDetail - 성공(비교과 관련)")
    @Test
    void getScheduleDetail_extra_success() {
        //given
        Schedule extraSchedule = Schedule.builder()
                .title("스케쥴1")
                .content("세부사항")
                .startDateTime(LocalDateTime.of(2025, 7, 1,0,0,0))
                .endDateTime(LocalDateTime.of(2025, 8, 1,0,0,0))
                .extracurricularId(extracurricular.getExtracurricularId())
                .build();
        when(scheduleRepository.findScheduleByMemberIdAndId(memberId, extraSchedule.getId()))
                .thenReturn(Optional.of(extraSchedule));
        when(extracurricularService.findByExtracurricularId(extracurricular.getExtracurricularId()))
                .thenReturn(Optional.of(extracurricular));
        //when
        GetScheduleDetailResponse result = scheduleService.getScheduleDetail(memberId, extraSchedule.getId());
        //then
        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, extraSchedule.getId());
        verify(extracurricularService).findByExtracurricularId(extracurricular.getExtracurricularId());
        GetScheduleDetailResponse expected = new GetScheduleDetailResponse(
                extraSchedule.getTitle(),
                extraSchedule.getContent(),
                EXTRACURRICULAR,
                extraSchedule.getStartDateTime(),
                extraSchedule.getEndDateTime(),
                new ExtracurricularField(
                        extracurricular.getTitle(),
                        extracurricular.getUrl(),
                        extracurricular.getApplicationStart(),
                        extracurricular.getApplicationEnd(),
                        extracurricular.getActivityStart(),
                        extracurricular.getActivityEnd()
                )
        );

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
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
                .extracting("title", "content", "scheduleType", "startDateTime", "endDateTime", "extracurricularField")
                .containsExactly(
                        schedule.getTitle(),
                        schedule.getContent(),
                        NORMAL,
                        schedule.getStartDateTime(),
                        schedule.getEndDateTime(),
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

    @Test
    @DisplayName("setScheduleDate - 활동기간이 있으면 활동기간으로 스케줄 세팅")
    void setScheduleDate_useActivityRange() {
        // given
        Long extraId = 1L;
        LocalDateTime start = LocalDateTime.of(2025, 7, 1, 9, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 7, 2, 9, 0);

        Extracurricular ext = Extracurricular.builder()
                .extracurricularId(extraId)
                .activityStart(start)
                .activityEnd(end)
                .applicationStart(LocalDateTime.of(2025, 6, 1, 9, 0))
                .applicationEnd(LocalDateTime.of(2025, 6, 2, 9, 0))
                .build();

        when(extracurricularService.getByExtracurricularId(extraId))
                .thenReturn(ext);

        Schedule schedule = Schedule.builder().build();

        // when
        scheduleService.setScheduleDate(extraId, schedule);

        // then
        assertThat(schedule.getStartDateTime()).isEqualTo(start);
        assertThat(schedule.getEndDateTime()).isEqualTo(end);
    }

    @Test
    @DisplayName("활동기간이 null이면 신청기간으로 스케줄 세팅")
    void setScheduleDate_useApplicationRangeWhenActivityNull() {
        // given
        Long extraId = 2L;
        LocalDateTime start = LocalDateTime.of(2025, 8, 1, 9, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 8, 2, 9, 0);

        Extracurricular ext = Extracurricular.builder()
                .extracurricularId(extraId)
                .activityStart(null)
                .activityEnd(null)
                .applicationStart(start)
                .applicationEnd(end)
                .build();

        when(extracurricularService.getByExtracurricularId(extraId))
                .thenReturn(ext);

        Schedule schedule = Schedule.builder().build();

        // when
        scheduleService.setScheduleDate(extraId, schedule);

        // then
        assertThat(schedule.getStartDateTime()).isEqualTo(start);
        assertThat(schedule.getEndDateTime()).isEqualTo(end);
    }

    @Test
    @DisplayName("활동/신청기간이 모두 없으면 now로 세팅")
    void setScheduleDate_useNowWhenNoDates() {
        // given
        Long extraId = 3L;
        Extracurricular ext = Extracurricular.builder()
                .extracurricularId(extraId)
                .activityStart(null).activityEnd(null)
                .applicationStart(null).applicationEnd(null)
                .build();

        when(extracurricularService.getByExtracurricularId(extraId))
                .thenReturn(ext);

        Schedule schedule = Schedule.builder().build();

        // when
        LocalDateTime before = LocalDateTime.now();
        scheduleService.setScheduleDate(extraId, schedule);
        LocalDateTime after  = LocalDateTime.now();

        // then (now는 호출 시점 차이가 있어 범위로 검증)
        assertThat(schedule.getStartDateTime()).isBetween(before.minusSeconds(1), after.plusSeconds(1));
        assertThat(schedule.getEndDateTime()).isBetween(before.minusSeconds(1), after.plusSeconds(1));
    }

    @Test
    @DisplayName("findAllByMyExtracurricular 메서드를 호출하는가")
    void getMyExtracurricular_shouldCallRepository() {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<Extracurricular> mockPage = new PageImpl<>(List.of());
        when(scheduleRepository.findAllByMyExtracurricular(memberId, pageable))
                .thenReturn(mockPage);

        // when
        Page<Extracurricular> result = scheduleService.getMyExtracurricular(memberId, pageable);

        // then
        assertThat(result).isSameAs(mockPage);
        verify(scheduleRepository, times(1))
                .findAllByMyExtracurricular(memberId, pageable);
    }
}
