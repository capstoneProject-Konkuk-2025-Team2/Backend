package com.capstone.backend.member.presentation;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.page.response.PageResponse;
import com.capstone.backend.core.common.web.response.ApiResponse;
import com.capstone.backend.member.dto.request.CreateReviewRequest;
import com.capstone.backend.member.dto.request.DeleteMyReviewRequest;
import com.capstone.backend.member.dto.response.ReviewResponse;
import com.capstone.backend.member.facade.ReviewFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "리뷰", description = "ReviewController")
public class ReviewController {
    private final ReviewFacade reviewFacade;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.REVIEW)
    @Operation(summary = "리뷰쓰기", description = "createReview")
    public ApiResponse<Boolean> createReview(
            @RequestBody CreateReviewRequest createReviewRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ApiResponse.success(reviewFacade.createReview(customUserDetails, createReviewRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.REVIEW)
    @Operation(summary = "리뷰조회", description = "viewReview")
    public ApiResponse<PageResponse<ReviewResponse>> viewReview(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        int safePage = (page < 1) ? 0 : page - 1;
        return ApiResponse.success(reviewFacade.viewReview(safePage, size));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.REVIEW_SEARCH)
    @Operation(summary = "리뷰검색", description = "searchReview")
    public ApiResponse<PageResponse<ReviewResponse>> searchReview(
            @RequestParam(name = "key") String key,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        int safePage = (page < 1) ? 0 : page - 1;
        return ApiResponse.success(reviewFacade.searchReview(key, safePage, size));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.MY_REVIEW)
    @Operation(summary = "내 리뷰 조회", description = "viewMyReview")
    public ApiResponse<PageResponse<ReviewResponse>> viewMyReview(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        int safePage = (page < 1) ? 0 : page - 1;
        return ApiResponse.success(reviewFacade.viewMyReview(customUserDetails, safePage, size));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.MY_REVIEW_SEARCH)
    @Operation(summary = "내 리뷰 검색", description = "searchMyReview")
    public ApiResponse<PageResponse<ReviewResponse>> searchMyReview(
            @RequestParam(name = "key") String key,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        int safePage = (page < 1) ? 0 : page - 1;
        return ApiResponse.success(reviewFacade.searchMyReview(customUserDetails, key, safePage, size));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(ApiPath.MY_REVIEW)
    @Operation(summary = "내 리뷰 삭제", description = "deleteMyReview")
    public ApiResponse<Boolean> deleteMyReview(
            @RequestBody DeleteMyReviewRequest deleteMyReviewRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ApiResponse.success(reviewFacade.deleteMyReview(customUserDetails, deleteMyReviewRequest));
    }
}
