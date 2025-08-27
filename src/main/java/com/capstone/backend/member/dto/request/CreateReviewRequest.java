package com.capstone.backend.member.dto.request;

import com.capstone.backend.member.domain.value.Star;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateReviewRequest(
        @Schema(description = "관련된 비교과 id", example = "1")
        Long extracurricularId,
        @Schema(description = "리뷰내용", example = "도움이 많이 됩니다.")
        String content,

        @NotBlank(message = "capstone.review.star.blank")
        @Schema(description = "별점", example = "ONE, TWO, THREE, FOUR, FIVE")
        Star star
) {
}
