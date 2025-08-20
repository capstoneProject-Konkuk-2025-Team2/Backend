package com.capstone.backend.extracurricular.domain.service;

import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.extracurricular.domain.repository.ExtracurricularRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<Extracurricular> findAllByTitle(String title, Pageable pageable) {
        return extracurricularRepository.findAllByTitle(title, pageable);
    }
}
