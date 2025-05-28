package com.capstone.backend.member.domain.repository;

import com.capstone.backend.member.domain.entity.Interest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    @Query("SELECT i FROM Interest i WHERE i.memberId = :memberId")
    List<Interest> findAllByMemberId(@Param("memberId") Long memberId);
}
