package com.capstone.backend.member.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.repository.TimetableRepository;
import com.capstone.backend.member.domain.service.TimetableService;
import com.capstone.backend.member.domain.value.Day;
import com.capstone.backend.member.dto.request.ChangeTimetableRequest;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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

    @DisplayName("findByMemberIdAndId - 성공")
    @Test
    void findByMemberIdAndId_success() {
        //given
        Timetable timetable = Timetable.builder().build();
        when(timetableRepository.findByMemberIdAndId(1L, 1L)).thenReturn(Optional.ofNullable(timetable));
        //when
        timetableService.findByMemberIdAndId(1L, 1L);
        //then
        verify(timetableRepository).findByMemberIdAndId(1L, 1L);
    }

    @DisplayName("changeTimetable - 성공")
    @Test
    void changeTimetable_success() {
        //given
        Timetable timetable = Timetable.builder()
                .id(1L)
                .memberId(1L)
                .day(Day.MON)
                .eventName("테스트 네임1")
                .eventDetail("테스트 디테일1")
                .startTime(LocalTime.of(13,0,0,0))
                .endTime(LocalTime.of(14,0,0,0))
                .color("#f6f6f6")
                .build();
        ChangeTimetableRequest changeTimetableRequest = new ChangeTimetableRequest(
                1L,
                Day.TUE,
                LocalTime.of(15,0,0,0),
                LocalTime.of(16,0,0,0),
                "수정된 네임1",
                "수정된 디테일1",
                "#r6r6r6"
        );
        //when
        timetableService.changeTimetable(timetable, changeTimetableRequest);
        //then
        assertEquals(timetable.getDay(), changeTimetableRequest.day());
        assertEquals(timetable.getEventName(), changeTimetableRequest.eventName());
        assertEquals(timetable.getEventDetail(), changeTimetableRequest.eventDetail());
        assertEquals(timetable.getStartTime(), changeTimetableRequest.startTime());
        assertEquals(timetable.getEndTime(), changeTimetableRequest.endTime());
        assertEquals(timetable.getColor(), changeTimetableRequest.color());
    }
}
