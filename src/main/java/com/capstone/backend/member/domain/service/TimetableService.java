package com.capstone.backend.member.domain.service;

import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.repository.TimetableRepository;
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

}
