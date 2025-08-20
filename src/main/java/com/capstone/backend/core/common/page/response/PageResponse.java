package com.capstone.backend.core.common.page.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
        @Schema(description = "페이지 응답")
        List<T> data,

        @Schema(description = "전체 페이지 수")
        int totalPages,

        @Schema(description = "마지막 페이지 여부")
        boolean isLastPage
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
