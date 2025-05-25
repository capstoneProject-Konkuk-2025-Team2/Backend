package com.capstone.backend.member.facade;

import static com.capstone.backend.core.auth.jwt.value.TokenInfo.ACCESS_TOKEN;
import static com.capstone.backend.member.domain.value.Role.MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.auth.jwt.JWTUtil;
import com.capstone.backend.core.infrastructure.mail.MailService;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.value.Role;
import com.capstone.backend.member.dto.request.SendAuthMailRequest;
import com.capstone.backend.member.dto.request.SetPasswordRequest;
import com.capstone.backend.member.dto.request.VerifyAuthCodeRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    private Member member;
    private CustomUserDetails customUserDetails;
    private HttpServletResponse httpServletResponse;
    @BeforeEach
    void setUp() {
        String email = "test@test.com";
        Role role = Role.TEMPORARY_MEMBER;
        member = Member.builder()
                .id(1L)
                .email(email)
                .role(role)
                .build();
        customUserDetails = Mockito.mock(CustomUserDetails.class);
        httpServletResponse = Mockito.mock(HttpServletResponse.class);
    }

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

    @DisplayName("비밀번호 설정")
    @Test
    void setPassword() {
        //given
        String accessToken = "access_token";
        String password = "password";
        String headerEncoding = "UTF-8";
        String headerContentType = "application/json; charset=UTF-8";
        SetPasswordRequest setPasswordRequest = new SetPasswordRequest(password);
        when(customUserDetails.getUsername()).thenReturn(member.getEmail());
        when(memberService.getByEmail(member.getEmail())).thenReturn(member);
        doNothing().when(memberService).updatePassword(member.getId(), password);
        doNothing().when(memberService).updateRole(member.getId(), MEMBER);
        when(jwtUtil.createJwt(ACCESS_TOKEN.category(), member.getEmail(), MEMBER.name(), ACCESS_TOKEN.expireMs()))
                .thenReturn(accessToken);
        doNothing().when(httpServletResponse).setCharacterEncoding(headerEncoding);
        doNothing().when(httpServletResponse).setContentType(headerContentType);
        doNothing().when(httpServletResponse).setHeader(ACCESS_TOKEN.category(), accessToken);
        //when
        onboardingFacade.setPassword(customUserDetails, setPasswordRequest, httpServletResponse);
        //then
        verify(memberService).getByEmail(member.getEmail());
        verify(memberService).updatePassword(member.getId(), setPasswordRequest.password());
        verify(memberService).updateRole(member.getId(), MEMBER);
        verify(jwtUtil).createJwt(ACCESS_TOKEN.category(), member.getEmail(), MEMBER.name(), ACCESS_TOKEN.expireMs());
        verify(httpServletResponse).setCharacterEncoding(headerEncoding);
        verify(httpServletResponse).setContentType(headerContentType);
        verify(httpServletResponse).setHeader(ACCESS_TOKEN.category(), accessToken);
    }
}
