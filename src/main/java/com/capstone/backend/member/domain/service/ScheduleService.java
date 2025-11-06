package com.capstone.backend.member.domain.service;

import com.capstone.backend.alarm.service.AlarmService;
import com.capstone.backend.core.common.web.response.ExtendedHttpStatus;
import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.extracurricular.domain.service.ExtracurricularService;
import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import com.capstone.backend.member.dto.request.DeleteScheduleRequest;
import com.capstone.backend.member.dto.request.ScheduleAlarmRequest;
import com.capstone.backend.member.dto.response.GetScheduleByYearAndMonthResponse;
import com.capstone.backend.member.dto.response.GetScheduleDetailResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ExtracurricularService extracurricularService;
    private final AlarmService alarmService;

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
        if((schedule.getStartDateTime() == null && schedule.getEndDateTime() == null) && changeScheduleRequest.extracurricularId() != null) {
            setScheduleDate(changeScheduleRequest.extracurricularId(), schedule);
        }
    }

    @Transactional
    public void deleteSchedule(Long memberId, DeleteScheduleRequest deleteScheduleRequest) {
        Schedule schedule = getByMemberIdAndId(memberId, deleteScheduleRequest.deleteScheduleId());
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public List<GetScheduleByYearAndMonthResponse> findByMemberIdAndYearAndMonth(Long memberId, Long year, Long month) {
        YearMonth ym = YearMonth.of(year.intValue(), month.intValue());
        LocalDateTime startInclusive = ym.atDay(1).atStartOfDay();
        LocalDateTime endExclusive = ym.plusMonths(1).atDay(1).atStartOfDay();
        return scheduleRepository
                .findByMemberIdAndOverlappingRange(memberId, startInclusive, endExclusive)
                .stream()
                .map(GetScheduleByYearAndMonthResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetScheduleDetailResponse getScheduleDetail(Long memberId, Long scheduleId) {
        Schedule schedule = getByMemberIdAndId(memberId, scheduleId);
        Extracurricular extracurricular = Optional.ofNullable(schedule.getExtracurricularId())
                .flatMap(extracurricularService::findByExtracurricularId)
                .orElse(null);
        return GetScheduleDetailResponse.of(schedule, extracurricular);
    }

    @Transactional
    public void putSchedule(Long memberId, CreateScheduleRequest createScheduleRequest) {
        Schedule schedule = Schedule.createSchedule(memberId, createScheduleRequest);
        if((schedule.getStartDateTime() == null && schedule.getEndDateTime() == null) && createScheduleRequest.extracurricularId() != null) {
            setScheduleDate(createScheduleRequest.extracurricularId(), schedule);
        }
        save(schedule);
    }

    @Transactional
    public void setScheduleDate(Long extracurricularId, Schedule schedule) {
        Extracurricular extracurricular = extracurricularService.getByExtracurricularId(extracurricularId);
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

    @Transactional(readOnly = true)
    public Page<Extracurricular> getMyExtracurricular(Long memberId, Pageable pageable) {
        return scheduleRepository.findAllByMyExtracurricular(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Extracurricular> searchMyExtracurricular(Long memberId, String key, Pageable pageable) {
        return scheduleRepository.findAllByMyExtracurricularAndTitle(memberId, key, pageable);
    }

    @Transactional
    public Boolean setAlarmSchedule(Long memberId, ScheduleAlarmRequest scheduleAlarmRequest) {
        Schedule schedule = getByMemberIdAndId(memberId, scheduleAlarmRequest.scheduleId());
        schedule.setAlarm(scheduleAlarmRequest.isAlarm());
        Duration offset = Duration.ofDays(1);
        if(scheduleAlarmRequest.isAlarm()) {
            Instant sendAt = schedule.getStartDateTime()
                    .atZone(ZoneId.of("Asia/Seoul")).toInstant()
                    .minus(offset);

            if (sendAt.isBefore(Instant.now())) {
                return true;
            }

            alarmService.enqueueSchedule(schedule, offset);
        }
        else {
            alarmService.disableNotification(memberId, schedule.getId());
        }
        return true;
    }
}
