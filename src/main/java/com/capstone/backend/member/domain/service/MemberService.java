package com.capstone.backend.member.domain.service;

import com.capstone.backend.core.infrastructure.exception.CustomException;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.repository.MemberRepository;
import com.capstone.backend.member.domain.value.Role;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
    public Member getByEmail(String email) {
        return findByEmail(email).orElseThrow(
                () ->
                        new CustomException("capstone.member.not.found"));
    }

    @Transactional(readOnly = true)
    public void isAlreadyPresentMemberEmail(String email) {
        if(findByEmail(email).isPresent()) {
            throw new CustomException("capstone.member.already.exists");
        }
    }

    @Transactional
    public void save(Member member){
        memberRepository.save(member);
    }

    @Transactional
    public void updatePassword(Long memberId, String password) {
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        Member member = getById(memberId);
        member.updatePassword(encodedPassword);
    }

    @Transactional
    public void updateRole(Long memberId, Role role) {
        Member member = getById(memberId);
        member.updateRole(role);
    }
}
