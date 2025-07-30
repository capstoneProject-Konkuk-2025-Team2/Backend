package com.capstone.backend.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capstone.backend.member.domain.entity.Schedule;
import com.capstone.backend.member.domain.repository.ScheduleRepository;
import com.capstone.backend.member.domain.value.ScheduleType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    @DisplayName("delete 테스트")
    @Test
    void delete_success() {
        // given
        Schedule schedule = Schedule.builder()
                .title("삭제 테스트")
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 7, 31))
                .memberId(1L)
                .build();
        Schedule saved = scheduleRepository.save(schedule);
        Long id = saved.getId();
        // when
        scheduleRepository.delete(saved);
        //then
        Optional<Schedule> result = scheduleRepository.findById(id);
        assertThat(result).isEmpty();
    }

    @DisplayName("findByMemberIdAndYearAndMonth 테스트")
    @Test
    void findByMemberIdAndYearAndMonth_success() {
        //given
        Long memberId = 1L;
        Schedule scheduleStartJuly = Schedule.builder().memberId(memberId)
                .startDate(LocalDate.of(2025, 7, 24))
                .endDate(LocalDate.of(2025, 7, 30))
                .build();
        Schedule scheduleEndJuly = Schedule.builder().memberId(memberId)
                .startDate(LocalDate.of(2025, 6, 24))
                .endDate(LocalDate.of(2025, 7, 30))
                .build();
        Schedule scheduleStartAndEndAugust = Schedule.builder().memberId(memberId)
                .startDate(LocalDate.of(2025, 8, 24))
                .endDate(LocalDate.of(2025, 8, 30))
                .build();
        List<Schedule> scheduleList = List.of(
                scheduleStartJuly,
                scheduleEndJuly,
                scheduleStartAndEndAugust
        );
        scheduleRepository.saveAll(scheduleList);
        // when
        List<Schedule> result = scheduleRepository.findByMemberIdAndYearAndMonth(1L, 2025L, 7L);
        //then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("startDate", "endDate").containsExactlyInAnyOrder(
                tuple(scheduleStartJuly.getStartDate(), scheduleStartJuly.getEndDate()),
                tuple(scheduleEndJuly.getStartDate(), scheduleEndJuly.getEndDate())
        );
    }
}
