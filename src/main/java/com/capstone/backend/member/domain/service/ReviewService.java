package com.capstone.backend.member.domain.service;

import com.capstone.backend.extracurricular.domain.service.ExtracurricularService;
import com.capstone.backend.member.domain.entity.Review;
import com.capstone.backend.member.domain.repository.ReviewRepository;
import com.capstone.backend.member.dto.request.CreateReviewRequest;
import com.capstone.backend.member.dto.request.DeleteMyReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ExtracurricularService extracurricularService;

    @Transactional
    public Review save(Long memberId, CreateReviewRequest createReviewRequest) {
        return reviewRepository.save(
                Review.createReview(memberId, createReviewRequest)
        );
    }

    @Transactional
    public void createReview(Long memberId, CreateReviewRequest createReviewRequest) {
        extracurricularService.getByExtracurricularId(createReviewRequest.extracurricularId());
        save(memberId, createReviewRequest);
    }

    @Transactional(readOnly = true)
    public Page<Review> viewReview(Pageable pageable) {
        return reviewRepository.findAllBy(pageable);
    }
    @Transactional(readOnly = true)
    public Page<Review> searchReview(String key, PageRequest pageRequest) {
        return reviewRepository.findAllByExtracurricular(key, pageRequest);
    }
    @Transactional(readOnly = true)
    public Page<Review> viewMyReview(Long memberId, PageRequest pageRequest) {
        return reviewRepository.findAllByMyReview(memberId, pageRequest);
    }
    @Transactional(readOnly = true)
    public Page<Review> searchMyReview(Long memberId, String key, PageRequest pageRequest) {
        return reviewRepository.findAllByMyReviewAndExtracurricular(memberId, key, pageRequest);
    }
    @Transactional
    public void deleteMyReview(Long memberId, DeleteMyReviewRequest deleteMyReviewRequest) {
        reviewRepository.deleteByIdAndMemberId(deleteMyReviewRequest.deleteReviewId(), memberId);
    }
}
