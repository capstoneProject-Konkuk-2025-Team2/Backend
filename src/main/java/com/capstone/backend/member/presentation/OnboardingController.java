package com.capstone.backend.member.presentation;

import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.member.dto.request.SendAuthMailRequest;
import com.capstone.backend.member.dto.request.VerifyAuthCodeRequest;
import com.capstone.backend.member.facade.OnboardingFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "온보딩", description = "OnboardingController")
public class OnboardingController {
    private final OnboardingFacade onboardingFacade;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.SEND_AUTH_MAIL)
    @Operation(summary = "메일 인증 번호 보내기", description = "sendAuthMail")
    public ApiResponse<Boolean> sendAuthMail(
            @RequestBody SendAuthMailRequest sendAuthMailRequest
    ) {
        onboardingFacade.sendAuthMail(sendAuthMailRequest);
        return ApiResponse.success(true);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(ApiPath.VERIFY_AUTH_CODE)
    @Operation(summary = "메일 인증 번호 확인", description = "sendAuthMail")
    public ApiResponse<Boolean> verifyAuthCode(
            @RequestBody VerifyAuthCodeRequest verifyAuthCodeRequest,
            HttpServletResponse response
    ) {
        return ApiResponse.success(onboardingFacade.verifyAuthCode(verifyAuthCodeRequest, response));
    }
}
