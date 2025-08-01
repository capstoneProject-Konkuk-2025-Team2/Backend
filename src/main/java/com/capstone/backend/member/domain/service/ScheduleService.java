package com.capstone.backend.member.domain.service;

import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Extracurricular;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import com.capstone.backend.member.dto.request.DeleteScheduleRequest;
import com.capstone.backend.member.dto.request.ExtracurricularField;
import com.capstone.backend.member.dto.response.GetScheduleByYearAndMonthResponse;
import com.capstone.backend.member.dto.response.GetScheduleDetailResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ExtracurricularService extracurricularService;

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
                () -> new CustomException(ExtendedHttpStatus.BAD_REQUEST, "capstone.schedule.not.found")
        );
    }

    @Transactional
    public void changeSchedule(Long memberId, ChangeScheduleRequest changeScheduleRequest) {
        Schedule schedule = getByMemberIdAndId(memberId, changeScheduleRequest.scheduleId());
        schedule.changeSchedule(changeScheduleRequest);
        Optional<ExtracurricularField> newFieldOpt = Optional.ofNullable(changeScheduleRequest.extracurricularField());
        Long currentExtraId = schedule.getExtracurricularId();
        newFieldOpt.ifPresentOrElse(
                newField -> {
                    if (currentExtraId == null) {
                        Long createdId = extracurricularService.createExtracurricular(newField).getId();
                        schedule.connectExtracurricular(createdId);
                    } else {
                        extracurricularService.changeExtracurricular(currentExtraId, newField);
                    }
                },
                () -> {
                    if (currentExtraId != null) {
                        extracurricularService.deleteExtracurricular(currentExtraId);
                        schedule.disconnectExtracurricular();
                    }
                }
        );
    }

    @Transactional
    public void deleteSchedule(Long memberId, DeleteScheduleRequest deleteScheduleRequest) {
        Schedule schedule = getByMemberIdAndId(memberId, deleteScheduleRequest.deleteScheduleId());
        Optional.ofNullable(schedule.getExtracurricularId())
                .ifPresent(extracurricularService::deleteExtracurricular);
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public List<GetScheduleByYearAndMonthResponse> findByMemberIdAndYearAndMonth(Long memberId, Long year, Long month) {
        return scheduleRepository.findByMemberIdAndYearAndMonth(memberId, year, month)
                .stream()
                .map(GetScheduleByYearAndMonthResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetScheduleDetailResponse getScheduleDetail(Long memberId, Long scheduleId) {
        Schedule schedule = getByMemberIdAndId(memberId, scheduleId);
        Extracurricular extracurricular = Optional.ofNullable(schedule.getExtracurricularId())
                .map(extracurricularService::getById)
                .orElse(null);
        return GetScheduleDetailResponse.of(schedule, extracurricular);
    }

    @Transactional
    public void putSchedule(Long memberId, CreateScheduleRequest createScheduleRequest) {
        Schedule schedule = Schedule.createSchedule(memberId, createScheduleRequest);
        save(schedule);
        Optional.ofNullable(createScheduleRequest.extracurricularField())
                .map(extracurricularService::createExtracurricular)
                .map(Extracurricular::getId)
                .ifPresent(schedule::connectExtracurricular);
    }
}
