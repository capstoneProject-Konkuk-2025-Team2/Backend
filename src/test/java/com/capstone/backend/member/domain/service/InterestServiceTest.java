package com.capstone.backend.member.domain.service;

import static org.mockito.Mockito.verify;

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
}
