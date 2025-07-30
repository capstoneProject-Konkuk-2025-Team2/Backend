package com.capstone.backend.member.domain.service;

import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Extracurricular;
import com.capstone.backend.member.domain.repository.ExtracurricularRepository;
import com.capstone.backend.member.dto.request.ExtracurricularField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExtraCurricularService {
    private final ExtracurricularRepository extraCurricularRepository;
    @Transactional
    public Extracurricular save(Extracurricular extracurricular) {
        return extraCurricularRepository.save(extracurricular);
    }

    @Transactional
    public Extracurricular createExtraCurricular(ExtracurricularField extracurricularField) {
        Extracurricular extracurricular = Extracurricular.createExtraCurricular(extracurricularField);
        return save(extracurricular);
    }

    @Transactional
    public void changeExtraCurricular(Long id, ExtracurricularField extracurricularField) {
        Extracurricular extracurricular = findById(id);
        extracurricular.changeExtracurricular(extracurricularField);
    }

    @Transactional(readOnly = true)
    public Extracurricular findById(Long id) {
        return extraCurricularRepository.findById(id).orElseThrow(
                () -> new CustomException("capstone.schedule.extra.not.found")
        );
    }

    @Transactional
    public void deleteExtraCurricular(Long id) {
        extraCurricularRepository.deleteById(id);
    }
}
