package com.capstone.backend.member.domain.entity;

import com.capstone.backend.global.entity.BaseEntity;
import com.capstone.backend.member.domain.value.Star;
import com.capstone.backend.member.dto.request.CreateReviewRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "REVIEW")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "EXTRACURRICULAR_ID")
    private Long extracurricularId;

    @Column(name = "CONTENT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "STAR")
    private Star star;

    public static Review createReview(Long memberId, CreateReviewRequest createReviewRequest) {
        return Review.builder()
                .memberId(memberId)
                .extracurricularId(createReviewRequest.extracurricularId())
                .content(createReviewRequest.content())
                .star(createReviewRequest.star())
                .build();
    }
}
