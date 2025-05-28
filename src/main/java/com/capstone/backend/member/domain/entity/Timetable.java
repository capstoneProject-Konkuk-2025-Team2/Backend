package com.capstone.backend.member.domain.entity;

import com.capstone.backend.global.entity.BaseEntity;
import com.capstone.backend.member.domain.value.Day;
import com.capstone.backend.member.dto.request.MakeMemberTimetableRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TIMETABLE")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Timetable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIMETABLE_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "DAY")
    private Day day;

    @Column(name = "START_TIME")
    private LocalTime startTime;

    @Column(name = "END_TIME")
    private LocalTime endTime;

    @Column(name = "EVENT_NAME", length = 30)
    private String eventName;

    @Column(name = "EVENT_DETAIL", length = 100)
    private String eventDetail;

    @Column(name = "COLOR", length = 50)
    private String color;

    public static Timetable makeTimetable(Long memberId, MakeMemberTimetableRequest makeMemberTimetableRequest) {
        return Timetable.builder()
                .memberId(memberId)
                .day(makeMemberTimetableRequest.day())
                .startTime(makeMemberTimetableRequest.startTime())
                .endTime(makeMemberTimetableRequest.endTime())
                .eventName(makeMemberTimetableRequest.eventName())
                .eventDetail(makeMemberTimetableRequest.eventDetail())
                .color(makeMemberTimetableRequest.color())
                .build();
    }

    public String getStringStartTime() {
        return formatTime(this.startTime);
    }

    public String getStringEndTime() {
        return formatTime(this.endTime);
    }

    private static String formatTime(LocalTime time) {
        return time != null ? time.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }
}
