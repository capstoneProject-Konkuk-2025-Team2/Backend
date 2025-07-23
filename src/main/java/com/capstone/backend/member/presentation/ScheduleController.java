package com.capstone.backend.member.presentation;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import com.capstone.backend.member.dto.request.DeleteScheduleRequest;
import com.capstone.backend.member.facade.ScheduleFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(ApiPath.CHANGE_SCHEDULE)
    @Operation(summary = "스케쥴 수정", description = "changeSchedule")
    public ApiResponse<Boolean> changeSchedule(
            @RequestBody @Valid ChangeScheduleRequest changeScheduleRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ApiResponse.success(scheduleFacade.changeSchedule(customUserDetails, changeScheduleRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(ApiPath.DELETE_SCHEDULE)
    @Operation(summary = "스케쥴 삭제", description = "deleteSchedule")
    public ApiResponse<Boolean> deleteSchedule(
            @RequestBody DeleteScheduleRequest deleteScheduleRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ApiResponse.success(scheduleFacade.deleteSchedule(customUserDetails, deleteScheduleRequest));
    }
}
