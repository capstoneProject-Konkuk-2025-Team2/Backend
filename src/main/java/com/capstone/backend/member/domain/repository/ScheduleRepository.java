package com.capstone.backend.member.domain.repository;

import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.member.domain.entity.Schedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findScheduleByMemberIdAndId(Long memberId, Long id);
    @Query(
            """
            SELECT s
            FROM Schedule s
            WHERE s.memberId = :memberId
             AND s.startDateTime < :endExclusive
             AND s.endDateTime >= :startInclusive
           """
    )
    List<Schedule> findByMemberIdAndOverlappingRange(
            @Param("memberId") Long memberId,
            @Param("startInclusive") java.time.LocalDateTime startInclusive,
            @Param("endExclusive") java.time.LocalDateTime endExclusive
    );

    @Query(
            """
            SELECT e
            FROM Schedule s
            JOIN Extracurricular e ON s.extracurricularId = e.extracurricularId
            WHERE s.memberId = :memberId
            AND s.extracurricularId IS NOT NULL
            """
    )
    Page<Extracurricular> findAllByMyExtracurricular(@Param("memberId") Long memberId, Pageable pageable);

    @Query(
            """
            SELECT e
            FROM Schedule s
            JOIN Extracurricular e ON s.extracurricularId = e.extracurricularId
            WHERE s.memberId = :memberId
            AND s.extracurricularId IS NOT NULL
            AND e.title LIKE %:key%
            """
    )
    Page<Extracurricular> findAllByMyExtracurricularAndTitle(@Param("memberId") Long memberId,@Param("key") String key, Pageable pageable);
}
