package com.capstone.backend.extracurricular.service;

import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.extracurricular.entity.Extracurricular;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.extracurricular.repository.ExtracurricularRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExtracurricularService {
    private final ExtracurricularRepository extracurricularRepository;
    @Transactional
    public Extracurricular save(Extracurricular extracurricular) {
        return extracurricularRepository.save(extracurricular);
    }

    @Transactional(readOnly = true)
    public Optional<Extracurricular> findByExtracurricularId(Long extracurricularId) {
        return extracurricularRepository.findByExtracurricularId(extracurricularId);
    }

    @Transactional(readOnly = true)
    public Extracurricular getByExtracurricularId(Long extracurricularId) {
        return findByExtracurricularId(extracurricularId).orElseThrow(
                () -> new CustomException("capstone.extra.not.found")
        );
    }
}
