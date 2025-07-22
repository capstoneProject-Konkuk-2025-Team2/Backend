package com.capstone.backend.member.presentation;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import com.capstone.backend.member.facade.ScheduleFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "스케쥴(캘린더)", description = "ScheduleController")
public class ScheduleController {
    private final ScheduleFacade scheduleFacade;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.ADD_SCHEDULE)
    @Operation(summary = "스케쥴 생성", description = "createSchedule")
    public ApiResponse<Boolean> createSchedule(
            @RequestBody @Valid CreateScheduleRequest createScheduleRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ApiResponse.success(scheduleFacade.createSchedule(customUserDetails, createScheduleRequest));
    }
}
