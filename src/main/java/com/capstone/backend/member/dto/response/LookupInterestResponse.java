package com.capstone.backend.member.dto.response;

import com.capstone.backend.member.domain.entity.Interest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record LookupInterestResponse(
        @Schema(description = "관심 사항 정보", example = "[\"AI\", \"프로그래밍\"]")
        List<String> interests
) {
    public static LookupInterestResponse of(
            List<Interest> interests
    ) {
        return new LookupInterestResponse(interests.stream()
                .map(Interest::getContent)
                .toList());
    }
}
