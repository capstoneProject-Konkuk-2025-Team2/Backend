package com.capstone.backend.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteMyReviewRequest(
        @Schema(description = "삭제할 리뷰 Id", example = "1")
        Long deleteReviewId
) {

}
