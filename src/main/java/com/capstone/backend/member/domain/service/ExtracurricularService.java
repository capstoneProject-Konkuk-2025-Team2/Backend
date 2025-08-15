package com.capstone.backend.member.domain.service;

import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Extracurricular;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ExtracurricularRepository;
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

    @Transactional
    public void isPresent(Long extracurricularId) {
        findByExtracurricularId(extracurricularId).orElseThrow(
                () -> new CustomException("capstone.extra.not.found")
        );
    }

    @Transactional(readOnly = true)
    public void setScheduleDate(Long extracurricularId, Schedule schedule) {
        Extracurricular extracurricular = getByExtracurricularId(extracurricularId);
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        if(extracurricular.getActivityStart() != null && extracurricular.getActivityEnd() != null) {
            startDateTime = extracurricular.getActivityStart();
            endDateTime = extracurricular.getActivityEnd();
        }
        else if(extracurricular.getApplicationStart() != null && extracurricular.getApplicationEnd() != null) {
            startDateTime = extracurricular.getApplicationStart();
            endDateTime = extracurricular.getApplicationEnd();
        }
        else {
            startDateTime = LocalDateTime.now();
            endDateTime = LocalDateTime.now();
        }
        schedule.setScheduleDateTime(startDateTime, endDateTime);
    }
}
