package com.capstone.backend.member.domain.service;

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
        return timetableRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Timetable findByMemberIdAndId(Long memberId, Long id) {
        return timetableRepository.findByMemberIdAndId(memberId, id);
    }

    @Transactional
    public void changeTimetable(Timetable timetable, ChangeTimetableRequest changeTimetableRequest) {
        timetable.changeTimetable(changeTimetableRequest);
    }
}
