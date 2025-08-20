package com.capstone.backend.extracurricular.domain.entity;

import com.capstone.backend.global.entity.BaseEntity;
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
@Table(name = "EXTRACURRICULAR")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Extracurricular extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXTRACURRICULAR_PK_ID")
    private Long id;

    @Column(name = "EXTRACURRICULAR_ID")
    private Long extracurricularId;

    @Column(name = "TITLE", length = 100)
    private String title;

    @Column(name = "URL", length = 500)
    private String url;

    @Column(name = "APPLICATION_START")
    private LocalDateTime applicationStart;

    @Column(name = "APPLICATION_END")
    private LocalDateTime applicationEnd;

    @Column(name = "ACTIVITY_START")
    private LocalDateTime activityStart;

    @Column(name = "ACTIVITY_END")
    private LocalDateTime activityEnd;
}
