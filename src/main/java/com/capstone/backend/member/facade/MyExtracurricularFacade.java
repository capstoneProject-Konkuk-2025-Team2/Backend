package com.capstone.backend.member.facade;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.page.response.PageResponse;
import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.extracurricular.dto.response.ExtracurricularResponse;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyExtracurricularFacade {
    private final ScheduleService scheduleService;
    private final MemberService memberService;

    public PageResponse<ExtracurricularResponse> lookupMyExtracurricular(CustomUserDetails customUserDetails, int page, int size) {
        Long memberId = memberService.getByEmail(customUserDetails.getUsername()).getId();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Extracurricular> extracurricularPage = scheduleService.getMyExtracurricular(memberId, pageRequest);
        Page<ExtracurricularResponse> searchExtracurricularResponsePage
                = extracurricularPage.map(ExtracurricularResponse::of);
        return PageResponse.from(searchExtracurricularResponsePage);
    }
}
