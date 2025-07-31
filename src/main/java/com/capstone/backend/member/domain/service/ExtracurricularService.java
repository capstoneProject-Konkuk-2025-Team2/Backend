package com.capstone.backend.member.domain.service;

import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Extracurricular;
import com.capstone.backend.member.domain.repository.ExtracurricularRepository;
import com.capstone.backend.member.dto.request.ExtracurricularField;
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

    @Transactional
    public Extracurricular createExtracurricular(ExtracurricularField extracurricularField) {
        Extracurricular extracurricular = Extracurricular.createExtraCurricular(extracurricularField);
        return save(extracurricular);
    }

    @Transactional
    public void changeExtracurricular(Long id, ExtracurricularField extracurricularField) {
        Extracurricular extracurricular = getById(id);
        extracurricular.changeExtracurricular(extracurricularField);
    }

    @Transactional(readOnly = true)
    public Optional<Extracurricular> findById(Long id) {
        return extracurricularRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Extracurricular getById(Long id) {
        return findById(id).orElseThrow(
                () -> new CustomException("capstone.schedule.extra.not.found")
        );
    }

    @Transactional
    public void deleteExtracurricular(Long id) {
        extracurricularRepository.deleteById(id);
    }
}
