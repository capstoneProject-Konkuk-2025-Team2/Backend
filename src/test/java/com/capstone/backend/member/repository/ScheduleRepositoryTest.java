package com.capstone.backend.member.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class ScheduleRepositoryTest {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("findScheduleByMemberIdAndId 테스트")
    @Test
    void findScheduleByMemberIdAndId_success() {
        //given
        List<Schedule> scheduleList = List.of(
                Schedule.builder().memberId(4L).title("AI 비교과").build(),
                Schedule.builder().memberId(4L).title("웹 비교과").build(),
                Schedule.builder().memberId(6L).title("경제 비교과").build()
        );
        scheduleRepository.saveAll(scheduleList);
        Long memberId = 4L;
        Long scheduleId = scheduleList.get(0).getId();
        //when
        Schedule schedule = scheduleRepository.findScheduleByMemberIdAndId(memberId, scheduleId).get();
        //then
        assertEquals(memberId, schedule.getMemberId());
        assertEquals(scheduleId, schedule.getId());
    }
}
