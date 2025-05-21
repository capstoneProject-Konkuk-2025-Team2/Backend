package com.capstone.backend.member.facade;

import com.capstone.backend.core.infrastructure.mail.MailService;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.dto.request.SendAuthMailRequest;
import com.capstone.backend.member.dto.request.VerifyAuthCodeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnboardingFacade {
    private final MailService mailService;
    private final MemberService memberService;
    public void sendAuthMail(SendAuthMailRequest sendAuthMailRequest) {
        mailService.sendVerificationCode(sendAuthMailRequest.email());
    }

    public Boolean verifyAuthCode(VerifyAuthCodeRequest verifyAuthCodeRequest) {
        if(!mailService.verifyCode(verifyAuthCodeRequest.email(), verifyAuthCodeRequest.code())){
            return false;
        }
        memberService.save(Member.createTemporaryMember(verifyAuthCodeRequest.email()));
        return true;
    }
}
