package com.capstone.backend.member.domain.entity;

import com.capstone.backend.global.entity.BaseEntity;
import com.capstone.backend.member.dto.request.ExtracurricularField;
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
    @Column(name = "EXTRACURRICULAR_ID")
    private Long id;

    @Column(name = "TITLE", length = 100)
    private String title;

    @Column(name = "URL", length = 100)
    private String url;

    @Column(name = "APPLICATION_START")
    private LocalDateTime applicationStart;

    @Column(name = "APPLICATION_END")
    private LocalDateTime applicationEnd;

    @Column(name = "ACTIVITY_START")
    private LocalDateTime activityStart;

    @Column(name = "ACTIVITY_END")
    private LocalDateTime activityEnd;

    public static Extracurricular createExtraCurricular(ExtracurricularField extracurricularField) {
        return Extracurricular.builder()
                .title(extracurricularField.originTitle())
                .url(extracurricularField.url())
                .applicationStart(extracurricularField.applicationStart())
                .applicationEnd(extracurricularField.applicationEnd())
                .activityStart(extracurricularField.activityStart())
                .activityEnd(extracurricularField.activityEnd())
                .build();
    }

    public void changeExtracurricular(ExtracurricularField extracurricularField) {
        this.title = extracurricularField.originTitle();
        this.url = extracurricularField.url();
        this.applicationStart = extracurricularField.applicationStart();
        this.applicationEnd = extracurricularField.applicationEnd();
        this.activityStart = extracurricularField.activityStart();
        this.activityEnd = extracurricularField.activityEnd();
    }
}
