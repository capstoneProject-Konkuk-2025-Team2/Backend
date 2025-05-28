package com.capstone.backend.member.domain.value;

public enum Day {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일");
    private final String korean;

    Day(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
