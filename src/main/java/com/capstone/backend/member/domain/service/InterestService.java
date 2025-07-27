package com.capstone.backend.member.domain.service;

import com.capstone.backend.member.domain.entity.Interest;
import com.capstone.backend.member.domain.repository.InterestRepository;
import com.capstone.backend.member.dto.request.InterestRequest;
import java.util.Arrays;
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

    @Transactional
    public void deleteAllByMemberId(Long memberId) {
        interestRepository.deleteAllByMemberId(memberId);
    }

    @Transactional
    public void create(Long memberId, InterestRequest interestRequest) {
        List<Interest> interestList = Arrays.stream(interestRequest.interestContent().split(","))
                .map(String::trim)
                .filter(interest -> !interest.isEmpty())
                .distinct()
                .map(req -> Interest.createInterest(memberId, req))
                .toList();
        saveAll(interestList);
    }

    @Transactional
    public void changeInterest(Long memberId, InterestRequest interestRequest) {
        deleteAllByMemberId(memberId);
        create(memberId, interestRequest);
    }
}
