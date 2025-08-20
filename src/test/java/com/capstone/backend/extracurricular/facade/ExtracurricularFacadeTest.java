package com.capstone.backend.extracurricular.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.common.page.response.PageResponse;
import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.extracurricular.domain.service.ExtracurricularService;
import com.capstone.backend.extracurricular.dto.response.SearchExtracurricularResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ExtracurricularFacadeTest {
    @Mock
    ExtracurricularService extracurricularService;

    @InjectMocks
    ExtracurricularFacade extracurricularFacade;

    @Test
    @DisplayName("키워드 검색 결과를 PageResponse로 매핑한다 - 정상 케이스")
    void searchExtracurricular_success() {
        // given
        int page = 0, size = 2;
        String key = "비교과";
        Pageable pageable = PageRequest.of(page, size);

        List<Extracurricular> content = List.of(
                Extracurricular.builder().title("A비교과").build(),
                Extracurricular.builder().title("B비교과").build()
        );
        Page<Extracurricular> stubPage = new PageImpl<>(content, pageable, 3);

        when(extracurricularService.findAllByTitle(key, pageable)).thenReturn(stubPage);

        // when
        PageResponse<SearchExtracurricularResponse> resp =
                extracurricularFacade.searchExtracurricular(page, size, key);

        // then
        assertThat(resp.data()).hasSize(2);
        assertThat(resp.totalPages()).isEqualTo(2);
        assertThat(resp.isLastPage()).isFalse();

        assertThat(resp.data().get(0).title()).contains("비교과");
        assertThat(resp.data().get(1).title()).contains("비교과");

        verify(extracurricularService, times(1)).findAllByTitle(key, pageable);
    }

    @Test
    @DisplayName("검색 결과가 비어도 PageResponse 메타데이터가 정상이어야 한다")
    void searchExtracurricular_empty() {
        // given
        int page = 0, size = 10;
        String key = "검색결과없음";
        Pageable pageable = PageRequest.of(page, size);

        Page<Extracurricular> empty = Page.empty(pageable);
        when(extracurricularService.findAllByTitle(key, pageable)).thenReturn(empty);

        // when
        PageResponse<SearchExtracurricularResponse> resp =
                extracurricularFacade.searchExtracurricular(page, size, key);

        // then
        assertThat(resp.data()).isEmpty();
        assertThat(resp.totalPages()).isEqualTo(0);
        assertThat(resp.isLastPage()).isTrue();

        verify(extracurricularService).findAllByTitle(key, pageable);
    }
}
