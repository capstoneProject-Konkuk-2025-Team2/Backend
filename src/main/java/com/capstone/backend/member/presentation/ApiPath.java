package com.capstone.backend.member.presentation;

public final class ApiPath {
    private ApiPath() {
        // 인스턴스화 방지용 private 생성자
    }

    public static final String SEND_AUTH_MAIL = "/v1/member/auth-mail";
    public static final String VERIFY_AUTH_CODE = "/v1/member/auth-code";
    public static final String SETTING_PASSWORD = "/v1/member/password";
}
