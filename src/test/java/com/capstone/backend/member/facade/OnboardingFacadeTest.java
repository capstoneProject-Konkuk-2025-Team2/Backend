package com.capstone.backend.member.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.auth.jwt.JWTUtil;
import com.capstone.backend.core.infrastructure.mail.MailService;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.dto.request.SendAuthMailRequest;
import com.capstone.backend.member.dto.request.VerifyAuthCodeRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OnboardingFacadeTest {
    @Mock
    private MailService mailService;

    @Mock
    private MemberService memberService;

    @Mock
    private HttpServletResponse response;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private OnboardingFacade onboardingFacade;

    @DisplayName("메일 인증 번호 보내기")
    @Test
    void sendAuthMail() {
        //given
        String email = "abc@def.com";
        SendAuthMailRequest sendAuthMailRequest = new SendAuthMailRequest(email);
        doNothing().when(mailService).sendVerificationCode(email);
        //when
        onboardingFacade.sendAuthMail(sendAuthMailRequest);
        //then
        verify(mailService).sendVerificationCode(email);
    }

    @DisplayName("메일 인증 번호 확인")
    @Test
    void verifyAuthCode() {
        //given
        String email = "abc@def.com";
        String code = "123456";
        VerifyAuthCodeRequest verifyAuthCodeRequest = new VerifyAuthCodeRequest(email, code);
        when(mailService.verifyCode(verifyAuthCodeRequest.email(), verifyAuthCodeRequest.code())).thenReturn(true);
        when(jwtUtil.createJwt(anyString(), anyString(), anyString(), anyLong())).thenReturn("token");
        Boolean result = onboardingFacade.verifyAuthCode(verifyAuthCodeRequest, response);
        verify(memberService).save(any());
        assertEquals(result, true);
    }
}
