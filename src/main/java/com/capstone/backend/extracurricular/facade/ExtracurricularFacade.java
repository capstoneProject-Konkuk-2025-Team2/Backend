package com.capstone.backend.extracurricular.facade;

import com.capstone.backend.core.common.page.response.PageResponse;
import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.extracurricular.domain.service.ExtracurricularService;
import com.capstone.backend.extracurricular.dto.response.SearchExtracurricularResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExtracurricularFacade {
    private final ExtracurricularService extracurricularService;

    public PageResponse<SearchExtracurricularResponse> searchExtracurricular(int page, int size, String key) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Extracurricular> extracurricularPage = extracurricularService.findAllByTitle(key, pageRequest);
        Page<SearchExtracurricularResponse> searchExtracurricularResponsePage
                = extracurricularPage.map(SearchExtracurricularResponse::of);
        return PageResponse.from(searchExtracurricularResponsePage);
    }
}
