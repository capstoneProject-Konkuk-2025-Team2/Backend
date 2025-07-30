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
import com.capstone.backend.member.domain.repository.ExtracurricularRepository;
import com.capstone.backend.member.dto.request.ExtracurricularField;
import java.time.LocalDateTime;
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
public class ExtraCurricularServiceTest {
    @Mock
    private ExtracurricularRepository extracurricularRepository;
    @Mock
    private AppEnv mockAppEnv;
    @Mock
    private ApplicationContext mockApplicationContext;

    @InjectMocks
    private ExtraCurricularService extraCurricularService;

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
        extraCurricularService.save(extracurricular);
        //then
        verify(extracurricularRepository).save(extracurricular);
    }

    @DisplayName("findById - 성공")
    @Test
    void findById_success() {
        //given
        Long extraCurricularId = extracurricular.getId();
        when(extracurricularRepository.findById(extraCurricularId)).thenReturn(Optional.of(extracurricular));
        //when
        extraCurricularService.findById(extraCurricularId);
        //then
        verify(extracurricularRepository).findById(extraCurricularId);
    }

    @DisplayName("findById - 실패(못 찾았을 때)")
    @Test
    void findById_fail_not_found() {
        //given
        Long extraCurricularId = extracurricular.getId();
        when(extracurricularRepository.findById(extraCurricularId)).thenReturn(Optional.empty());
        // when & then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> extraCurricularService.findById(extraCurricularId)
        );
        ApiError error = exception.getError();
        assertThat(error.element().code().value()).isEqualTo("capstone.schedule.extra.not.found");
    }

    @DisplayName("createExtraCurricular - 성공")
    @Test
    void createExtraCurricular_success() {
        //given
        ExtracurricularField field = new ExtracurricularField(
                "비교과A",
                "https://abc.com",
                LocalDateTime.of(2025, 8, 1, 9, 0),
                LocalDateTime.of(2025, 8, 2, 9, 0),
                LocalDateTime.of(2025, 8, 6, 9, 0),
                LocalDateTime.of(2025, 8, 6, 12, 0)
        );
        //when
        extraCurricularService.createExtraCurricular(field);
        //then
        ArgumentCaptor<Extracurricular> extracurricularCaptor = ArgumentCaptor.forClass(Extracurricular.class);
        verify(extracurricularRepository).save(extracurricularCaptor.capture());
        Extracurricular savedExtracurricular = extracurricularCaptor.getValue();
        assertThat(savedExtracurricular.getTitle()).isEqualTo(field.originTitle());
        assertThat(savedExtracurricular.getUrl()).isEqualTo(field.url());
        assertThat(savedExtracurricular.getApplicationStart()).isEqualTo(field.applicationStart());
        assertThat(savedExtracurricular.getApplicationEnd()).isEqualTo(field.applicationEnd());
        assertThat(savedExtracurricular.getActivityStart()).isEqualTo(field.activityStart());
        assertThat(savedExtracurricular.getActivityEnd()).isEqualTo(field.activityEnd());
    }

    @DisplayName("changeExtraCurricular - 성공")
    @Test
    void changeExtraCurricular_success() {
        //given
        ExtracurricularField field = new ExtracurricularField(
                "비교과A",
                "https://abc.com",
                LocalDateTime.of(2025, 8, 1, 9, 0),
                LocalDateTime.of(2025, 8, 2, 9, 0),
                LocalDateTime.of(2025, 8, 6, 9, 0),
                LocalDateTime.of(2025, 8, 6, 12, 0)
        );
        Long extracurricularId = extracurricular.getId();
        when(extracurricularRepository.findById(extracurricularId)).thenReturn(Optional.of(extracurricular));
        //when
        extraCurricularService.changeExtraCurricular(extracurricularId, field);
        //then
        assertThat(extracurricular.getTitle()).isEqualTo(field.originTitle());
        assertThat(extracurricular.getUrl()).isEqualTo(field.url());
        assertThat(extracurricular.getApplicationStart()).isEqualTo(field.applicationStart());
        assertThat(extracurricular.getApplicationEnd()).isEqualTo(field.applicationEnd());
        assertThat(extracurricular.getActivityStart()).isEqualTo(field.activityStart());
        assertThat(extracurricular.getActivityEnd()).isEqualTo(field.activityEnd());
    }

    @DisplayName("deleteExtraCurricular - 성공")
    @Test
    void deleteExtraCurricular_success() {
        //given
        Long extracurricularId = extracurricular.getId();
        //when
        extraCurricularService.deleteExtraCurricular(extracurricularId);
        //then
        verify(extracurricularRepository).deleteById(extracurricularId);
    }
}
