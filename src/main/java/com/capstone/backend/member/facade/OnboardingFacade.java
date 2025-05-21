package com.capstone.backend.member.facade;

import com.capstone.backend.core.infrastructure.mail.MailService;
import com.capstone.backend.member.dto.request.SendAuthMailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnboardingFacade {
    private final MailService mailService;
    public void sendAuthMail(SendAuthMailRequest sendAuthMailRequest) {
        mailService.sendVerificationCode(sendAuthMailRequest.email());
    }
}
