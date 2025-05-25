package com.capstone.backend.core.auth.jwt.value;

public enum TokenInfo {
    // 첫 번째 인자 : token category 이름, 두 번째 인자 : 만료시간
    ACCESS_TOKEN("access_token", 3600000L), // 만료시간 1시간
    REFRESH_TOKEN("refresh_token", 86400000L); // 만료시간 24시간

    private final String category;
    private final Long expireMs;

    TokenInfo(String category, Long expireMs) {
        this.category = category;
        this.expireMs = expireMs;
    }

    public String category(){
        return category;
    }

    public Long expireMs(){
        return expireMs;
    }
}
