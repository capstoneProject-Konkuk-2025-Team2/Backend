package com.capstone.backend.domain.member.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.repository.TimetableRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class TimetableRepositoryTest {
    @Autowired
    private TimetableRepository timetableRepository;

    @DisplayName("findByMemberIdAndId 테스트")
    @Test
    void findByMemberIdAndId_success() {
        //given
        List<Timetable> timetableList = List.of(
                Timetable.builder().memberId(4L).build(),
                Timetable.builder().memberId(5L).build(),
                Timetable.builder().memberId(6L).build()
        );
        timetableRepository.saveAll(timetableList);
        Long timeTableId = timetableList.get(0).getId();
        //when
        Timetable timetable = timetableRepository.findByMemberIdAndId(4L , timeTableId).get();
        //then
        assertEquals(4L, timetable.getMemberId());
        assertEquals(timeTableId , timetable.getId());
    }
}
