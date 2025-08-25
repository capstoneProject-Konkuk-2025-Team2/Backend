package com.capstone.backend.member.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.extracurricular.domain.repository.ExtracurricularRepository;
import com.capstone.backend.member.domain.entity.Schedule;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class ScheduleRepositoryTest {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ExtracurricularRepository extracurricularRepository;

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
                .startDateTime(LocalDateTime.of(2025, 7, 1, 0, 0, 0))
                .endDateTime(LocalDateTime.of(2025, 7, 31,0,0,0))
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

    @DisplayName("findByMemberIdAndOverlappingRange 테스트")
    @Test
    void findByMemberIdAndOverlappingRange_success() {
        //given
        Long memberId = 1L;
        Schedule scheduleStartJuly = Schedule.builder().memberId(memberId)
                .startDateTime(LocalDateTime.of(2025, 7, 24, 0, 0,0))
                .endDateTime(LocalDateTime.of(2025, 7, 30, 0, 0,0))
                .build();
        Schedule scheduleEndJuly = Schedule.builder().memberId(memberId)
                .startDateTime(LocalDateTime.of(2025, 6, 24,0,0,0))
                .endDateTime(LocalDateTime.of(2025, 7, 30,0,0,0))
                .build();
        Schedule scheduleStartAndEndAugust = Schedule.builder().memberId(memberId)
                .startDateTime(LocalDateTime.of(2025, 8, 24,0,0,0))
                .endDateTime(LocalDateTime.of(2025, 8, 30,0,0,0))
                .build();
        List<Schedule> scheduleList = List.of(
                scheduleStartJuly,
                scheduleEndJuly,
                scheduleStartAndEndAugust
        );
        scheduleRepository.saveAll(scheduleList);
        YearMonth ym = YearMonth.of(2025,7);
        LocalDateTime startInclusive = ym.atDay(1).atStartOfDay();
        LocalDateTime endExclusive = ym.plusMonths(1).atDay(1).atStartOfDay();
        // when
        List<Schedule> result = scheduleRepository.findByMemberIdAndOverlappingRange(1L, startInclusive, endExclusive);
        //then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("startDateTime", "endDateTime").containsExactlyInAnyOrder(
                tuple(scheduleStartJuly.getStartDateTime(), scheduleStartJuly.getEndDateTime()),
                tuple(scheduleEndJuly.getStartDateTime(), scheduleEndJuly.getEndDateTime())
        );
    }

    @DisplayName("findAllByMyExtracurricular 테스트")
    @Test
    void findAllByMyExtracurricular_success() {
        // given
        Long memberId = 1L;

        Extracurricular e1 = extracurricularRepository.save(
                Extracurricular.builder().title("비교과A").extracurricularId(1L).build()
        );
        Extracurricular e2 = extracurricularRepository.save(
                Extracurricular.builder().title("비교과B").extracurricularId(2L).build()
        );

        Schedule s1 = Schedule.builder()
                .memberId(memberId)
                .extracurricularId(e1.getExtracurricularId())
                .title("스터디 일정")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();

        Schedule s2 = Schedule.builder()
                .memberId(memberId)
                .extracurricularId(e2.getExtracurricularId())
                .title("세미나 일정")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();

        Schedule s3 = Schedule.builder()
                .memberId(memberId)
                .extracurricularId(null) // 제외 대상
                .title("개인 일정")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .build();

        scheduleRepository.save(s1);
        scheduleRepository.save(s2);
        scheduleRepository.save(s3);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Extracurricular> page =
                scheduleRepository.findAllByMyExtracurricular(memberId, pageable);

        // then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent())
                .extracting(Extracurricular::getTitle)
                .containsExactlyInAnyOrder("비교과A", "비교과B");
    }

    @DisplayName("LIKE 검색 + 페이징 기본 동작")
    @Test
    void findAllByMyExtracurricularAndTitle_like_with_paging() {
        //given
        Long memberId = 1L;
        Extracurricular e1 = extracurricularRepository.save(
                Extracurricular.builder().title("비교과A").extracurricularId(1L).build()
        );
        Extracurricular e2 = extracurricularRepository.save(
                Extracurricular.builder().title("비교과B").extracurricularId(2L).build()
        );
        Extracurricular e3 = extracurricularRepository.save(
                Extracurricular.builder().title("비교과C").extracurricularId(3L).build()
        );
        Schedule s1 = Schedule.builder()
                .memberId(memberId)
                .extracurricularId(e1.getExtracurricularId())
                .title("스터디 일정")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();

        Schedule s2 = Schedule.builder()
                .memberId(memberId)
                .extracurricularId(e2.getExtracurricularId())
                .title("세미나 일정")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();

        Schedule s3 = Schedule.builder()
                .memberId(memberId)
                .extracurricularId(e3.getExtracurricularId())
                .title("개인 일정")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .build();
        scheduleRepository.save(s1);
        scheduleRepository.save(s2);
        scheduleRepository.save(s3);

        String key = "비교과";
        Pageable pageable = PageRequest.of(0, 2);
        // when
        Page<Extracurricular> page = scheduleRepository.findAllByMyExtracurricularAndTitle(memberId, key, pageable);
        // then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();

        assertThat(page.getContent().get(0).getTitle()).contains(key);
        assertThat(page.getContent().get(1).getTitle()).contains(key);
    }

    @Test
    @DisplayName("검색 결과가 없을 때도 Page 메타데이터가 정상이어야 한다")
    void findAllByMyExtracurricularAndTitle_noResult() {
        // given
        Long memberId = 1L;
        Schedule s = Schedule.builder()
                .memberId(memberId)
                .extracurricularId(null)
                .title("스터디 일정")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(2))
                .build();
        scheduleRepository.save(s);
        Pageable pageable = PageRequest.of(0, 10);
        String key = "비교과";
        // when
        Page<Extracurricular> page = scheduleRepository.findAllByMyExtracurricularAndTitle(memberId, key, pageable);
        // then
        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getTotalPages()).isZero();
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isTrue();
    }
}
