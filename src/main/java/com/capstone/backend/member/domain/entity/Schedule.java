package com.capstone.backend.member.domain.entity;

import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SCHEDULE")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "EXTRACURRICULAR_ID")
    private Long extracurricularId;

    @Column(name = "START_DATE_TIME")
    private LocalDateTime startDateTime;

    @Column(name = "END_DATE_TIME")
    private LocalDateTime endDateTime;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    public static Schedule createSchedule(Long memberId, CreateScheduleRequest createScheduleRequest) {
        return Schedule.builder()
                .memberId(memberId)
                .startDateTime(createScheduleRequest.startDateTime())
                .endDateTime(createScheduleRequest.endDateTime())
                .title(createScheduleRequest.title())
                .content(createScheduleRequest.content())
                .extracurricularId(createScheduleRequest.extracurricularId())
                .build();
    }

    public void changeSchedule(ChangeScheduleRequest changeScheduleRequest) {
        this.startDateTime = changeScheduleRequest.startDateTime();
        this.endDateTime = changeScheduleRequest.endDateTime();
        this.title = changeScheduleRequest.title();
        this.content = changeScheduleRequest.content();
        this.extracurricularId = changeScheduleRequest.extracurricularId();
    }

    public void setScheduleDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
