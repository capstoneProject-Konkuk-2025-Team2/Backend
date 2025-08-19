package com.capstone.backend.extracurricular.repository;

import com.capstone.backend.extracurricular.entity.Extracurricular;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExtracurricularRepository extends JpaRepository<Extracurricular, Long> {
    @Query("SELECT e FROM Extracurricular e WHERE e.extracurricularId = :extracurricularId")
    Optional<Extracurricular> findByExtracurricularId(@Param("extracurricularId") Long extracurricularId);
}
