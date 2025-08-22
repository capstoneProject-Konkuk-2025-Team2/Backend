package com.capstone.backend.member.presentation;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.page.response.PageResponse;
import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.extracurricular.dto.response.ExtracurricularResponse;
import com.capstone.backend.member.facade.MyExtracurricularFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "내가 추가한 비교과 활동 관련 컨트롤러", description = "MyExtracurricularController")
public class MyExtracurricularController {
    private final MyExtracurricularFacade myExtracurricularFacade;
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.MY_EXTRACURRICULAR)
    @Operation(summary = "내가 추가한 비교과 활동 조회", description = "lookupMyExtracurricular")
    public ApiResponse<PageResponse<ExtracurricularResponse>> lookupMyExtracurricular(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ApiResponse.success(myExtracurricularFacade.lookupMyExtracurricular(customUserDetails, page, size));
    }
}
