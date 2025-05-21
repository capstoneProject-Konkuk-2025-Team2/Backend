package com.capstone.backend.core.infrastructure.mail;

import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private static final String AUTH_CODE_PREFIX = "AuthCode:";
    private static final String AUTH_REQUEST_PREFIX = "AuthRequest:";

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberService memberService;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.mail_auth_code_expiration}")
    private long authCodeExpirationMillis;

    @Value("${spring.mail.username}")
    private String mailSenderAddress;

    @Async("mailTaskExecutor")
    public void sendVerificationCode(String email) {
        memberService.isAlreadyPresentMemberEmail(email);

        String title = "졸업 프로젝트 이메일 인증 번호";
        String authCode = generateAuthCode();

        // 5분 내에 인증 코드 요청이 있었는지 확인
        if (redisTemplate.hasKey(AUTH_REQUEST_PREFIX + email)) {
            log.error("이메일 전송 실패: email={}, error={}", email, "5분 뒤에 다시 시도해주세요");
            throw new CustomException("capstone.mail.too.many.request");
        }

        try {
            sendMail(email, title, authCode);

            // Redis에 인증 코드 저장 (key: AuthCode + email, value: authCode)
            redisTemplate.opsForValue().set(
                    AUTH_CODE_PREFIX + email,
                    authCode,
                    Duration.ofMillis(authCodeExpirationMillis)
            );
            redisTemplate.opsForValue().set(
                    AUTH_REQUEST_PREFIX + email,
                    "REQUESTED",
                    Duration.ofMinutes(5)
            );

            log.info("이메일 인증 코드 전송 성공: {}", email);
        } catch (Exception e) {
            log.error("이메일 전송 실패: email={}, error={}", email, e.getMessage(), e);
            // 이메일 전송 실패 시 Redis에 저장된 인증 코드 삭제
            redisTemplate.delete(AUTH_CODE_PREFIX + email);
            throw new CustomException(ExtendedHttpStatus.INTERNAL_SERVER_ERROR, "capstone.mail.send.fail");
        }
    }

    /**
     * 이메일 전송
     */
    private void sendMail(String toEmail, String title, String authCode) throws MessagingException {
        MimeMessage emailForm = createEmailForm(toEmail, title, authCode);
        mailSender.send(emailForm);
    }

    /**
     * 이메일 템플릿 생성
     */
    private MimeMessage createEmailForm(String toEmail, String title, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Thymeleaf 템플릿 처리
        Context context = new Context();
        context.setVariable("verificationCode", authCode);
        String htmlContent = templateEngine.process("verification-email", context);

        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(htmlContent, true);
        helper.setFrom(mailSenderAddress);

        log.info("발신자 이메일 주소: {}", mailSenderAddress);
        return message;
    }

    // 이메일 인증 코드 검증
    public Boolean verifyCode(String email, String authCode) {
        memberService.isAlreadyPresentMemberEmail(email);

        if(!redisTemplate.hasKey(AUTH_CODE_PREFIX + email)){
            log.warn("이메일 인증 실패: email={}, 입력 코드={}", email, authCode);
            return false;
        }

        String redisAuthCode = redisTemplate.opsForValue().get(AUTH_CODE_PREFIX + email).toString();

        if (redisAuthCode == null || !redisAuthCode.equals(authCode)) {
            log.warn("이메일 인증 실패: email={}, 입력 코드={}, Redis 코드={}", email, authCode, redisAuthCode);
            return false;
        }

        log.info("이메일 인증 성공: email={}", email);
        return true;
    }

    // 랜덤 인증 코드 6자리 생성
    private String generateAuthCode() {
        return new SecureRandom().ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
