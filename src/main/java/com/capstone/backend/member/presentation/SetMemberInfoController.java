package com.capstone.backend.member.presentation;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.member.dto.request.ChangeTimetableRequest;
import com.capstone.backend.member.dto.request.CreateAcademicInfoRequest;
import com.capstone.backend.member.dto.request.InterestRequest;
import com.capstone.backend.member.dto.request.MakeMemberTimetableRequest;
import com.capstone.backend.member.facade.SetMemberInfoFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "내 정보 설정 컨트롤러", description = "SetMemberInfoController")
public class SetMemberInfoController {
    private final SetMemberInfoFacade setMemberInfoFacade;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.MAKE_TIMETABLE)
    @Operation(summary = "시간표 만들기", description = "makeMemberTimetable")
    public ApiResponse<Boolean> makeTimetable(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody List<MakeMemberTimetableRequest> makeMemberTimetableRequestList
    ){
        return ApiResponse.success(setMemberInfoFacade.makeTimetable(customUserDetails, makeMemberTimetableRequestList));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.SETTING_INTEREST)
    @Operation(summary = "관심사항 입력받기", description = "createInterestInfo")
    public ApiResponse<Boolean> createInterestInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid InterestRequest createInterestInfoRequest
    ){
        return ApiResponse.success(setMemberInfoFacade.createInterestInfo(customUserDetails, createInterestInfoRequest));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.SETTING_ACADEMIC_INFO)
    @Operation(summary = "학적정보 입력 받기", description = "createAcademicInfo")
    public ApiResponse<Boolean> createAcademicInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CreateAcademicInfoRequest createAcademicInfoRequest
    ) {
        return ApiResponse.success(setMemberInfoFacade.createAcademicInfo(customUserDetails, createAcademicInfoRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(ApiPath.CHANGE_TIMETABLE)
    @Operation(summary = "시간표 변경", description = "changeTimetable")
    public ApiResponse<Boolean> changeTimetable(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ChangeTimetableRequest changeTimetableRequest
    ) {
        return ApiResponse.success(setMemberInfoFacade.changeTimetable(customUserDetails, changeTimetableRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(ApiPath.CHANGE_INTEREST)
    @Operation(summary = "관심사항 변경", description = "changeInterest")
    public ApiResponse<Boolean> changeInterest(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody InterestRequest interestRequest
    ) {
        return ApiResponse.success(setMemberInfoFacade.changeInterest(customUserDetails, interestRequest));
    }
}
