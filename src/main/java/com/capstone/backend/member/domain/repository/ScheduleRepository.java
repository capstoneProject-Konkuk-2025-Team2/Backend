package com.capstone.backend.member.domain.repository;

import com.capstone.backend.member.domain.entity.Schedule;
import java.util.List;
import java.util.Optional;
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
            AND (
                (function('YEAR', s.startDateTime) = :year AND function('MONTH', s.startDateTime) = :month)
                OR
                (function('YEAR', s.endDateTime) = :year AND function('MONTH', s.endDateTime) = :month)
            )
        """
    )
    List<Schedule> findByMemberIdAndYearAndMonth(@Param("memberId") Long memberId, @Param("year") Long year, @Param("month") Long month);
}
