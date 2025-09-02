package com.capstone.backend.member.facade;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.page.response.PageResponse;
import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.extracurricular.domain.service.ExtracurricularService;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Review;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.ReviewService;
import com.capstone.backend.member.dto.request.CreateReviewRequest;
import com.capstone.backend.member.dto.request.DeleteMyReviewRequest;
import com.capstone.backend.member.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewFacade {
    private final ReviewService reviewService;
    private final MemberService memberService;
    private final ExtracurricularService extracurricularService;

    public boolean createReview(CustomUserDetails customUserDetails, CreateReviewRequest createReviewRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        reviewService.createReview(member.getId(), createReviewRequest);
        return true;
    }

    public PageResponse<ReviewResponse> viewReview(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewService.viewReview(pageRequest);
        Page<ReviewResponse> reviewResponsePage
                = reviewPage.map(review -> {
            Extracurricular extracurricular = extracurricularService.getByExtracurricularId(review.getExtracurricularId());
            return ReviewResponse.of(review, extracurricular.getTitle());
        });
        return PageResponse.from(reviewResponsePage);
    }

    public PageResponse<ReviewResponse> searchReview(String key, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewService.searchReview(key, pageRequest);
        Page<ReviewResponse> reviewResponsePage
                = reviewPage.map(review -> {
            Extracurricular extracurricular = extracurricularService.getByExtracurricularId(
                    review.getExtracurricularId());
            return ReviewResponse.of(review, extracurricular.getTitle());
        });
        return PageResponse.from(reviewResponsePage);
    }

    public PageResponse<ReviewResponse> viewMyReview(CustomUserDetails customUserDetails, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        Page<Review> reviewPage = reviewService.viewMyReview(member.getId(), pageRequest);
        Page<ReviewResponse> reviewResponsePage
                = reviewPage.map(review -> {
            Extracurricular extracurricular = extracurricularService.getByExtracurricularId(
                    review.getExtracurricularId());
            return ReviewResponse.of(review, extracurricular.getTitle());
        });
        return PageResponse.from(reviewResponsePage);
    }

    public PageResponse<ReviewResponse> searchMyReview(CustomUserDetails customUserDetails, String key, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        Page<Review> reviewPage = reviewService.searchMyReview(member.getId(), key, pageRequest);
        Page<ReviewResponse> reviewResponsePage
                = reviewPage.map(review -> {
            Extracurricular extracurricular = extracurricularService.getByExtracurricularId(
                    review.getExtracurricularId());
            return ReviewResponse.of(review, extracurricular.getTitle());
        });
        return PageResponse.from(reviewResponsePage);
    }

    public Boolean deleteMyReview(CustomUserDetails customUserDetails, DeleteMyReviewRequest deleteMyReviewRequest) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        reviewService.deleteMyReview(member.getId(), deleteMyReviewRequest);
        return true;
    }
}
