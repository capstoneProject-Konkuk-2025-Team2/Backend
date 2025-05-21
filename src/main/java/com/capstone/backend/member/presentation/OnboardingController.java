package com.capstone.backend.member.presentation;

import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.member.dto.request.SendAuthMailRequest;
import com.capstone.backend.member.facade.OnboardingFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ApiResponse<Boolean> sendAuthMail(
            @RequestBody SendAuthMailRequest sendAuthMailRequest
    ) {
        onboardingFacade.sendAuthMail(sendAuthMailRequest);
        return ApiResponse.success(true);
    }
}
