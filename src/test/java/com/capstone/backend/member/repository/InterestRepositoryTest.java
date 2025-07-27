package com.capstone.backend.member.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.capstone.backend.member.domain.entity.Interest;
import com.capstone.backend.member.domain.repository.InterestRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class InterestRepositoryTest {
    @Autowired
    private InterestRepository interestRepository;

    @DisplayName("saveAll 테스트")
    @Test
    void saveAll_success() {
        //given
        List<Interest> interestList = List.of(
                Interest.builder().content("AI").build(),
                Interest.builder().content("웹").build(),
                Interest.builder().content("경제").build()
        );
        //when
        interestRepository.saveAll(interestList);
        //then
        List<Interest> interests = interestRepository.findAll();
        assertEquals(interestList.size(), interests.size());
        List<String> actualContents = interests.stream()
                .map(Interest::getContent)
                .toList();
        assertThat(actualContents, containsInAnyOrder("AI", "웹", "경제"));
    }

    @DisplayName("findAllByMemberId 테스트")
    @Test
    void findByMemberIdAndId_success() {
        //given
        List<Interest> interestList = List.of(
                Interest.builder().memberId(4L).content("AI").build(),
                Interest.builder().memberId(4L).content("웹").build(),
                Interest.builder().memberId(6L).content("경제").build()
        );
        interestRepository.saveAll(interestList);
        //when
        List<Interest> actualInterestList = interestRepository.findAllByMemberId(4L);
        //then
        assertEquals(2, actualInterestList.size());
        assertTrue(actualInterestList.stream().allMatch(i -> i.getMemberId().equals(4L)));
        List<String> actualContents = actualInterestList.stream()
                .map(Interest::getContent)
                .toList();
        assertThat(actualContents, containsInAnyOrder("AI", "웹"));
    }

    @DisplayName("deleteAllByMemberId 테스트")
    @Test
    void deleteAllByMemberId_success() {
        //given
        List<Interest> interestList = List.of(
                Interest.builder().memberId(4L).content("AI").build(),
                Interest.builder().memberId(4L).content("웹").build(),
                Interest.builder().memberId(4L).content("경제").build()
        );
        interestRepository.saveAll(interestList);
        //when
        interestRepository.deleteAllByMemberId(4L);
        //then
        List<Interest> actualInterestList = interestRepository.findAllByMemberId(4L);
        assertEquals(0, actualInterestList.size());
    }
}
