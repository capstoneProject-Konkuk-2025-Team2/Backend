package com.capstone.backend.member.presentation;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.member.dto.response.LookupTimetableResponse;
import com.capstone.backend.member.facade.LookupFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "멤버 정보 조회", description = "LookUpMemberInfoController")
public class LookUpMemberInfoController {
    private final LookupFacade lookupFacade;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.LOOKUP_TIMETABLE)
    @Operation(summary = "내 시간표 조회", description = "lookUptimeTable")
    public ApiResponse<List<LookupTimetableResponse>> lookupTimetable(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ApiResponse.success(lookupFacade.lookupTimetable(customUserDetails));
    }

}
