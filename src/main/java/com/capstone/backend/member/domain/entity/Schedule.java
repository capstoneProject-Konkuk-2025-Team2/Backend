package com.capstone.backend.member.domain.entity;

import com.capstone.backend.member.domain.value.ScheduleType;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
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

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    public static Schedule createSchedule(Long memberId, CreateScheduleRequest createScheduleRequest) {
        return Schedule.builder()
                .memberId(memberId)
                .startDate(createScheduleRequest.startDate())
                .endDate(createScheduleRequest.endDate())
                .title(createScheduleRequest.title())
                .content(createScheduleRequest.content())
                .build();
    }

    public void changeSchedule(ChangeScheduleRequest changeScheduleRequest) {
        this.startDate = changeScheduleRequest.startDate();
        this.endDate = changeScheduleRequest.endDate();
        this.title = changeScheduleRequest.title();
        this.content = changeScheduleRequest.content();
    }
}
