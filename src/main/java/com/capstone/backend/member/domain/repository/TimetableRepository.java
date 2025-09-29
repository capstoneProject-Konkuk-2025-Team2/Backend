package com.capstone.backend.member.domain.repository;

import com.capstone.backend.member.domain.entity.Timetable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    @Query("SELECT t FROM Timetable t WHERE t.memberId = :memberId")
    Optional<List<Timetable>> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT t FROM Timetable t WHERE t.memberId =:memberId AND t.id = :id")
    Optional<Timetable> findByMemberIdAndId(@Param("memberId") Long memberId, @Param("id") Long id);

    void deleteByMemberIdAndId(Long memberId, Long timetableId);
}
