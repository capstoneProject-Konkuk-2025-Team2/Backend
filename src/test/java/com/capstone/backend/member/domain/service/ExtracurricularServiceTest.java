package com.capstone.backend.member.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.common.support.SpringContextHolder;
import com.capstone.backend.core.common.web.response.exception.ApiError;
import com.capstone.backend.core.configuration.env.AppEnv;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Extracurricular;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ExtracurricularRepository;
import java.time.LocalDateTime;
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
public class ExtracurricularServiceTest {
    @Mock
    private ExtracurricularRepository extracurricularRepository;
    @Mock
    private AppEnv mockAppEnv;
    @Mock
    private ApplicationContext mockApplicationContext;

    @InjectMocks
    private ExtracurricularService extracurricularService;

    private Extracurricular extracurricular;
    @BeforeEach
    void setup() {
        SpringContextHolder.setContextForTesting(mockApplicationContext);
        lenient().when(mockApplicationContext.getBean(AppEnv.class)).thenReturn(mockAppEnv);
        lenient().when(mockAppEnv.getId()).thenReturn("test-env");
        extracurricular = Extracurricular.builder()
                .title("비교과A")
                .url("https://abc.cdf")
                .applicationStart(LocalDateTime.of(2025,8,1,9,0))
                .applicationEnd(LocalDateTime.of(2025,8,2,9,0))
                .activityStart(LocalDateTime.of(2025,8,6,9,0))
                .activityEnd(LocalDateTime.of(2025,8,6,12,0))
                .build();
    }

    @DisplayName("save - 성공")
    @Test
    void save_success() {
        //when
        extracurricularService.save(extracurricular);
        //then
        verify(extracurricularRepository).save(extracurricular);
    }

    @DisplayName("findByExtracurricularId - 성공")
    @Test
    void findById_success() {
        //given
        Long extraCurricularId = extracurricular.getExtracurricularId();
        when(extracurricularRepository.findByExtracurricularId(extraCurricularId)).thenReturn(Optional.of(extracurricular));
        //when
        extracurricularService.findByExtracurricularId(extraCurricularId);
        //then
        verify(extracurricularRepository).findByExtracurricularId(extraCurricularId);
    }

    @DisplayName("getByExtracurricularId - 성공")
    @Test
    void getById_success() {
        //given
        Long extraCurricularId = extracurricular.getExtracurricularId();
        when(extracurricularRepository.findByExtracurricularId(extraCurricularId)).thenReturn(Optional.of(extracurricular));
        //when
        extracurricularService.getByExtracurricularId(extraCurricularId);
        //then
        verify(extracurricularRepository).findByExtracurricularId(extraCurricularId);
    }

    @DisplayName("getByExtracurricularId - 실패(못 찾았을 때)")
    @Test
    void findById_fail_not_found() {
        //given
        Long extraCurricularId = extracurricular.getExtracurricularId();
        when(extracurricularRepository.findByExtracurricularId(extraCurricularId)).thenReturn(Optional.empty());
        // when & then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> extracurricularService.getByExtracurricularId(extraCurricularId)
        );
        ApiError error = exception.getError();
        assertThat(error.element().code().value()).isEqualTo("capstone.extra.not.found");
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

        when(extracurricularRepository.findByExtracurricularId(extraId))
                .thenReturn(Optional.of(ext));

        Schedule schedule = Schedule.builder().build();

        // when
        extracurricularService.setScheduleDate(extraId, schedule);

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

        when(extracurricularRepository.findByExtracurricularId(extraId))
                .thenReturn(Optional.of(ext));

        Schedule schedule = Schedule.builder().build();

        // when
        extracurricularService.setScheduleDate(extraId, schedule);

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

        when(extracurricularRepository.findByExtracurricularId(extraId))
                .thenReturn(Optional.of(ext));

        Schedule schedule = Schedule.builder().build();

        // when
        LocalDateTime before = LocalDateTime.now();
        extracurricularService.setScheduleDate(extraId, schedule);
        LocalDateTime after  = LocalDateTime.now();

        // then (now는 호출 시점 차이가 있어 범위로 검증)
        assertThat(schedule.getStartDateTime()).isBetween(before.minusSeconds(1), after.plusSeconds(1));
        assertThat(schedule.getEndDateTime()).isBetween(before.minusSeconds(1), after.plusSeconds(1));
    }
}
