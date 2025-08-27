package com.capstone.backend.member.domain.repository;

import com.capstone.backend.member.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(
            """
            SELECT r
            FROM Review r
            """
    )
    Page<Review> findAllBy(Pageable pageable);
    @Query(
            """
            SELECT r
            FROM Review r
            JOIN Extracurricular e ON r.extracurricularId = e.extracurricularId
            WHERE e.extracurricularId IS NOT NULL
            AND e.title LIKE %:key%
            """
    )
    Page<Review> findAllByExtracurricular(@Param("key")String key, PageRequest pageRequest);

    @Query(
            """
            SELECT r
            FROM Review r
            WHERE r.memberId = :memberId
            """
    )
    Page<Review> findAllByMyReview(@Param("memberId")Long memberId, PageRequest pageRequest);

    @Query(
            """
            SELECT r
            FROM Review r
            JOIN Extracurricular e ON r.extracurricularId = e.extracurricularId
            WHERE e.extracurricularId IS NOT NULL
            AND r.memberId = :memberId
            AND e.title LIKE %:key%
            """
    )
    Page<Review> findAllByMyReviewAndExtracurricular(@Param("memberId")Long memberId, @Param("key")String key, PageRequest pageRequest);

    void deleteByIdAndMemberId(Long id, Long memberId);
}
