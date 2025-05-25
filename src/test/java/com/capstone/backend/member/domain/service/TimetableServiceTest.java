package com.capstone.backend.member.domain.service;

import static org.mockito.Mockito.verify;

import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.repository.TimetableRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimetableServiceTest {
    @Mock
    private TimetableRepository timetableRepository;

    @InjectMocks
    private TimetableService timetableService;

    @DisplayName("saveAll - 성공")
    @Test
    void saveAll_success() {
        //given
        List<Timetable> timetableList = List.of(
                Timetable.builder().id(1L).build(),
                Timetable.builder().id(2L).build(),
                Timetable.builder().id(3L).build()
        );
        //when
        timetableService.saveAll(timetableList);
        //then
        verify(timetableRepository).saveAll(timetableList);
    }
}
