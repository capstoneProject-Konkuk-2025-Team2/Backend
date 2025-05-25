package com.capstone.backend.member.facade;

import static com.capstone.backend.core.auth.jwt.value.TokenInfo.ACCESS_TOKEN;
import static com.capstone.backend.member.domain.value.Role.MEMBER;
import static com.capstone.backend.member.domain.value.Role.TEMPORARY_MEMBER;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.auth.jwt.JWTUtil;
import com.capstone.backend.core.infrastructure.mail.MailService;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.dto.request.SendAuthMailRequest;
import com.capstone.backend.member.dto.request.SetPasswordRequest;
import com.capstone.backend.member.dto.request.VerifyAuthCodeRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OnboardingFacade {
    private final MailService mailService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    public void sendAuthMail(SendAuthMailRequest sendAuthMailRequest) {
        mailService.sendVerificationCode(sendAuthMailRequest.email());
    }

    public Boolean verifyAuthCode(VerifyAuthCodeRequest verifyAuthCodeRequest, HttpServletResponse response) {
        if(!mailService.verifyCode(verifyAuthCodeRequest.email(), verifyAuthCodeRequest.code())){
            return false;
        }
        memberService.save(Member.createTemporaryMember(verifyAuthCodeRequest.email()));

        String accessToken = jwtUtil.createJwt(ACCESS_TOKEN.category(), verifyAuthCodeRequest.email(), TEMPORARY_MEMBER.name(), ACCESS_TOKEN.expireMs());
        response.setCharacterEncoding("UTF-8");  // ✅ 응답 인코딩을 UTF-8로 설정
        response.setContentType("application/json; charset=UTF-8");  // ✅ Content-Type 설정
        response.setHeader(ACCESS_TOKEN.category(), accessToken);
        return true;
    }

    @Transactional
    public Boolean setPassword(CustomUserDetails customUserDetails, SetPasswordRequest setPasswordRequest, HttpServletResponse response) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        memberService.updatePassword(member.getId(), setPasswordRequest.password());
        memberService.updateRole(member.getId(), MEMBER);
        String accessToken = jwtUtil.createJwt(ACCESS_TOKEN.category(), member.getEmail(), MEMBER.name(), ACCESS_TOKEN.expireMs());
        response.setCharacterEncoding("UTF-8");  // ✅ 응답 인코딩을 UTF-8로 설정
        response.setContentType("application/json; charset=UTF-8");  // ✅ Content-Type 설정
        response.setHeader(ACCESS_TOKEN.category(), accessToken);
        return true;
    }
}
