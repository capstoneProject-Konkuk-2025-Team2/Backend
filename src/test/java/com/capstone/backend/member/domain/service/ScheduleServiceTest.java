package com.capstone.backend.member.domain.service;

import static org.mockito.Mockito.verify;

import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @DisplayName("save - 성공")
    @Test
    void save_success() {
        //given
        Schedule schedule = Schedule.builder().build();
        //when
        scheduleService.save(schedule);
        //then
        verify(scheduleRepository).save(schedule);
    }
}
