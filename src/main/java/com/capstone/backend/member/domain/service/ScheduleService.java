package com.capstone.backend.member.domain.service;

import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.DeleteScheduleRequest;
import java.util.Optional;
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

    @Transactional(readOnly = true)
    public Optional<Schedule> findByMemberIdAndId(Long memberId, Long scheduleId) {
        return scheduleRepository.findScheduleByMemberIdAndId(memberId, scheduleId);
    }

    @Transactional(readOnly = true)
    public Schedule getByMemberIdAndId(Long memberId, Long scheduleId) {
        return findByMemberIdAndId(memberId, scheduleId).orElseThrow(
                () -> new CustomException("capstone.schedule.not.found")
        );
    }

    @Transactional
    public void changeSchedule(Long memberId, ChangeScheduleRequest changeScheduleRequest) {
        Schedule schedule = getByMemberIdAndId(memberId, changeScheduleRequest.scheduleId());
        schedule.changeSchedule(changeScheduleRequest);
    }

    @Transactional
    public void deleteSchedule(Long memberId, DeleteScheduleRequest deleteScheduleRequest) {
        Schedule schedule = getByMemberIdAndId(memberId, deleteScheduleRequest.deleteScheduleId());
        scheduleRepository.delete(schedule);
    }
}
