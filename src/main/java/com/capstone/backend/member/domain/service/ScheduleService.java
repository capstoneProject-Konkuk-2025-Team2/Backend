package com.capstone.backend.member.domain.service;

import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }
}
