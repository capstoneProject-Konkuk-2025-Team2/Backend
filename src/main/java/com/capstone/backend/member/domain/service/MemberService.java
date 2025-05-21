package com.capstone.backend.member.domain.service;

import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getById(Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new CustomException("capstone.member.not.found"));
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
    @Transactional(readOnly = true)
    public void isAlreadyPresentMemberEmail(String email) {
        if(findByEmail(email).isPresent()) {
            throw new CustomException("capstone.member.not.found");
        }
    }
}
