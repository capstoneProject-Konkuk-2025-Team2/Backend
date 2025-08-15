package com.capstone.backend.member.domain.repository;

import com.capstone.backend.member.domain.entity.Extracurricular;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExtracurricularRepository extends JpaRepository<Extracurricular, Long> {
    @Query("SELECT e FROM Extracurricular e WHERE e.extracurricularId = :extracurricularId")
    Optional<Extracurricular> findByExtracurricularId(@Param("extracurricularId") Long extracurricularId);
}
