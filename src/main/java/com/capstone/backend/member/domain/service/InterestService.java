package com.capstone.backend.member.domain.service;

import com.capstone.backend.member.domain.entity.Interest;
import com.capstone.backend.member.domain.repository.InterestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;

    @Transactional
    public void saveAll(List<Interest> interestList) {
        interestRepository.saveAll(interestList);
    }

    @Transactional(readOnly = true)
    public List<Interest> findAllByMemberId(Long memberId) {
        return interestRepository.findAllByMemberId(memberId);
    }
}
