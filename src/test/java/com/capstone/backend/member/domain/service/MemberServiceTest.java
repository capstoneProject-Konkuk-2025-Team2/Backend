package com.capstone.backend.member.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("getById - 성공")
    @Test
    void getById_success() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                        .id(memberId)
                        .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        // when
        Member result = memberService.getById(memberId);

        // then
        assertThat(result.getId()).isEqualTo(memberId);
        verify(memberRepository).findById(memberId);
    }

    @DisplayName("findById - 성공")
    @Test
    void findById_success() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .id(memberId)
                .build();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        // when
        memberService.findById(memberId);

        //then
        verify(memberRepository).findById(memberId);
    }

    @DisplayName("findByEmail - 성공")
    @Test
    void findByEmail_success() {
        //given
        Long memberId = 1L;
        String email = "abc@def.com";
        Member member = Member.builder()
                .id(memberId)
                .email(email)
                .build();
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        // when
        memberService.findByEmail(email);

        //then
        verify(memberRepository).findByEmail(email);
    }

    @DisplayName("isAlreadyPresentMemberEmail - 성공")
    @Test
    void isAlreadyPresentMemberEmail_success() {
        //given
        String email = "abc@def.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        // when
        memberService.isAlreadyPresentMemberEmail(email);
        //then
        verify(memberRepository).findByEmail(email);
    }

    @DisplayName("save - 성공")
    @Test
    void save_success(){
        //given
        Long memberId = 1L;
        String email = "abc@def.com";
        Member member = Member.builder()
                .id(memberId)
                .email(email)
                .build();
        //when
        memberService.save(member);
        //then
        verify(memberRepository).save(member);
    }
}
