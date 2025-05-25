package com.capstone.backend.core.auth.jwt;

import static com.capstone.backend.core.auth.jwt.value.TokenInfo.ACCESS_TOKEN;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import com.capstone.backend.core.common.web.response.exception.ErrorCodeResolvingApiErrorException;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //클라이언트 요청에서 사번, 비밀번호 추출
        Map<String, String> requestBody;
        try {
            requestBody = new ObjectMapper().readValue(request.getInputStream(), new TypeReference<>() {});
        } catch (IOException e) {
            throw new CustomException("capstone.common.invalid.request");
        }

        String email = requestBody.get("email");
        String password = requestBody.get("password");

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        //token에 담은 검증을 위한 AuthenticationManager로 전달

        return authenticationManager.authenticate(authToken);

    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException {
        //유저 정보
        String email = ((CustomUserDetails)authentication.getPrincipal()).getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt(ACCESS_TOKEN.category(), email, role, ACCESS_TOKEN.expireMs());

        //응답 설정
        response.setCharacterEncoding("UTF-8");  // ✅ 응답 인코딩을 UTF-8로 설정
        response.setContentType("application/json; charset=UTF-8");  // ✅ Content-Type 설정
        response.setHeader(ACCESS_TOKEN.category(), access);
        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Pair<ExtendedHttpStatus,String> error;

        if (failed instanceof UsernameNotFoundException) {
            error = Pair.of(ExtendedHttpStatus.NOT_FOUND,"capstone.member.not.found");
        } else if (failed instanceof BadCredentialsException) {
            error = Pair.of(ExtendedHttpStatus.FORBIDDEN, "capstone.member.wrong.password");
        } else {
            error = Pair.of(ExtendedHttpStatus.BAD_REQUEST, "capstone.common.invalid.request");
        }

        // ErrorResponse 객체 생성
        ErrorCodeResolvingApiErrorException exception = new ErrorCodeResolvingApiErrorException(
                error.getLeft(),
                error.getRight()
        );

        // JSON 응답 반환
        try {
            new ObjectMapper().writeValue(response.getWriter(), exception);
        } catch (IOException e) {
            throw new RuntimeException("Response 출력 중 오류 발생", e);
        }
    }
}
