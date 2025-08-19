package com.capstone.backend.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.member.domain.entity.Interest;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.repository.InterestRepository;

import com.capstone.backend.member.domain.service.InterestService;
import com.capstone.backend.member.dto.request.InterestRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InterestServiceTest {
    @Mock
    private InterestRepository interestRepository;

    @InjectMocks
    private InterestService interestService;

    private Member member;
    @BeforeEach
    void setup() {
        member = Member.builder().build();
    }

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
        Long memberId = member.getId();
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
        assertThat(result).extracting("id")
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @DisplayName("deleteAllByMemberId - 성공")
    @Test
    void deleteAllByMemberId_success() {
        //given
        Long memberId = member.getId();
        //when
        interestService.deleteAllByMemberId(memberId);
        //then
        verify(interestRepository).deleteAllByMemberId(memberId);
    }

    @DisplayName("create - 성공")
    @Test
    void create_success() {
        // given
        Long memberId = 1L;
        String content = "AI, 웹, 경제";
        InterestRequest request = new InterestRequest(content);
        // when
        interestService.create(memberId, request);
        // then
        ArgumentCaptor<List<Interest>> captor = ArgumentCaptor.captor();
        verify(interestRepository).saveAll(captor.capture());
        List<Interest> saved = captor.getValue();
        assertThat(saved).hasSize(3);
        assertThat(saved).extracting("content")
                .containsExactlyInAnyOrder("AI", "웹", "경제");
        assertThat(saved).allMatch(i -> i.getMemberId().equals(memberId));
    }

    @DisplayName("changeInterest - 성공")
    @Test
    void changeInterest_success() {
        //given
        Long memberId = 1L;
        String content = "AI, 웹, 경제";
        InterestRequest request = new InterestRequest(content);
        //when
        interestService.changeInterest(memberId, request);
        //then
        ArgumentCaptor<Long> memberIdCaptor = ArgumentCaptor.captor();
        ArgumentCaptor<List<Interest>> interestListCaptor = ArgumentCaptor.captor();
        verify(interestRepository).deleteAllByMemberId(memberIdCaptor.capture());
        assertThat(memberIdCaptor.getValue()).isEqualTo(memberId);
        verify(interestRepository).saveAll(interestListCaptor.capture());
        List<Interest> saved = interestListCaptor.getValue();
        assertThat(saved).hasSize(3);
        assertThat(saved).extracting("content")
                .containsExactlyInAnyOrder("AI", "웹", "경제");
        assertThat(saved).allMatch(i -> i.getMemberId().equals(memberId));
    }

}
