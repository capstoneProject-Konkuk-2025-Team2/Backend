package com.capstone.backend.member.dto.response;

import com.capstone.backend.member.domain.entity.Review;
import com.capstone.backend.member.domain.value.Star;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewResponse(
        @Schema(description = "리뷰ID", example = "1")
        Long reviewId,
        @Schema(description = "관련된 비교과 ID", example = "1")
        Long extracurricularId,
        @Schema(description = "관련된 비교과 제목", example = "A비교과")
        String title,
        @Schema(description = "리뷰내용", example = "좋아요!")
        String content,
        @Schema(description = "별점", example = "FIVE")
        Star star
) {
    public static ReviewResponse of(
            Review review,
            String title
    ) {
        return new ReviewResponse(
                review.getId(),
                review.getExtracurricularId(),
                title,
                review.getContent(),
                review.getStar()
        );
    }
}
