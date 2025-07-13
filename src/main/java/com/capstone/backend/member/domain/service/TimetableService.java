package com.capstone.backend.member.domain.service;

import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.repository.TimetableRepository;
import com.capstone.backend.member.dto.request.ChangeTimetableRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimetableService {
    private final TimetableRepository timetableRepository;

    @Transactional
    public void saveAll(List<Timetable> timetableList) {
        timetableRepository.saveAll(timetableList);
    }

    @Transactional(readOnly = true)
    public List<Timetable> findAllByMemberId(Long memberId) {
        return timetableRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(ExtendedHttpStatus.BAD_REQUEST, "capstone.timetable.not.found"));
    }

    @Transactional(readOnly = true)
    public Timetable findByMemberIdAndId(Long memberId, Long id) {
        return timetableRepository.findByMemberIdAndId(memberId, id).orElseThrow(() -> new CustomException(ExtendedHttpStatus.BAD_REQUEST, "capstone.timetable.not.found"));
    }

    @Transactional
    public void changeTimetable(Timetable timetable, ChangeTimetableRequest changeTimetableRequest) {
        timetable.changeTimetable(changeTimetableRequest);
    }
}
