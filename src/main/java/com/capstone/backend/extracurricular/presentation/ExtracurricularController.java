package com.capstone.backend.extracurricular.presentation;

import com.capstone.backend.core.common.page.response.PageResponse;
import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.extracurricular.dto.response.SearchExtracurricularResponse;
import com.capstone.backend.extracurricular.facade.ExtracurricularFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "비교과 관련", description = "ExtracurricularController")
public class ExtracurricularController {
    private final ExtracurricularFacade extracurricularFacade;
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(ApiPath.SEARCH_EXTRACURRICULAR)
    @Operation(summary = "비교과 검색", description = "searchExtracurricular")
    public ApiResponse<PageResponse<SearchExtracurricularResponse>> searchExtracurricular(
            @RequestParam(name = "key") String key,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        int safePage = (page < 1) ? 0 : page - 1;
        return ApiResponse.success(extracurricularFacade.searchExtracurricular(safePage, size, key));
    }
}
