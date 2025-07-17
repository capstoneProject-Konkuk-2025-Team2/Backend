package com.capstone.backend.member.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.member.domain.entity.Interest;
import com.capstone.backend.member.domain.repository.InterestRepository;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InterestServiceTest {
    @Mock
    private InterestRepository interestRepository;

    @InjectMocks
    private InterestService interestService;

    @DisplayName("saveAll - 성공")
    @Test
    void saveAll_success() {
        //given
        List<Interest> interestList = List.of(
                Interest.builder().id(1L).build(),
                Interest.builder().id(2L).build(),
                Interest.builder().id(3L).build()
        );
        //when
        interestService.saveAll(interestList);
        //then
        verify(interestRepository).saveAll(interestList);
    }

    @DisplayName("findAllByMemberId - 성공")
    @Test
    void findAllByMemberId_success() {
        //given
        Long memberId = 1L;
        List<Interest> expected = List.of(
                Interest.builder().id(1L).build(),
                Interest.builder().id(2L).build(),
                Interest.builder().id(3L).build()
        );
        when(interestRepository.findAllByMemberId(memberId)).thenReturn(
                expected
        );
        //when
        List<Interest> result = interestService.findAllByMemberId(memberId);
        //then
        verify(interestRepository).findAllByMemberId(memberId);
        assertEquals(result.size(), 3);
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), result.get(i).getId());
        }
    }
}
