package com.capstone.backend.domain.extracurricular.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.common.support.SpringContextHolder;
import com.capstone.backend.core.common.web.response.exception.ApiError;
import com.capstone.backend.core.configuration.env.AppEnv;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.extracurricular.entity.Extracurricular;
import com.capstone.backend.extracurricular.service.ExtracurricularService;
import com.capstone.backend.extracurricular.repository.ExtracurricularRepository;
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
}
