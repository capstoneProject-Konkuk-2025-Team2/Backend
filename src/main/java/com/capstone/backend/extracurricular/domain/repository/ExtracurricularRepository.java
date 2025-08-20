package com.capstone.backend.extracurricular.domain.repository;

import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExtracurricularRepository extends JpaRepository<Extracurricular, Long> {
    @Query("SELECT e FROM Extracurricular e WHERE e.extracurricularId = :extracurricularId")
    Optional<Extracurricular> findByExtracurricularId(@Param("extracurricularId") Long extracurricularId);

    @Query("""
           SELECT e
           FROM Extracurricular e
           WHERE e.title LIKE %:key%
           """)
    Page<Extracurricular> findAllByTitle(@Param("key") String key, Pageable pageable);
}
