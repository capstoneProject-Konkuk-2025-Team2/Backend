package com.capstone.backend.member.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.common.support.SpringContextHolder;
import com.capstone.backend.core.configuration.env.AppEnv;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import com.capstone.backend.member.domain.value.ScheduleType;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.DeleteScheduleRequest;
import com.capstone.backend.member.dto.response.GetScheduleByYearAndMonthResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

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
                .scheduleType(ScheduleType.EXTRACURRICULAR)
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
        assertThatThrownBy(() -> scheduleService.getByMemberIdAndId(memberId, schedule.getId()))
                .isInstanceOf(CustomException.class);

        verify(scheduleRepository).findScheduleByMemberIdAndId(memberId, schedule.getId());
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
        assertThat(schedule.getScheduleType()).isEqualTo(request.scheduleType());
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
                .scheduleType(ScheduleType.EXTRACURRICULAR)
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 8, 1))
                .build();
        Schedule schedule2 = Schedule.builder()
                .memberId(memberId)
                .title("비교과2")
                .scheduleType(ScheduleType.NORMAL)
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
        assertThat(result).extracting("scheduleId","title","scheduleType","startDate","endDate").containsExactlyInAnyOrder(
                tuple(schedule1.getId(), schedule1.getTitle(), schedule1.getScheduleType(), schedule1.getStartDate(), schedule1.getEndDate()),
                tuple(schedule2.getId(), schedule2.getTitle(), schedule2.getScheduleType(), schedule2.getStartDate(), schedule2.getEndDate())
        );
    }
}
