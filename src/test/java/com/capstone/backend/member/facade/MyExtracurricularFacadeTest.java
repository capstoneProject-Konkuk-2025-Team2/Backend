package com.capstone.backend.member.facade;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.core.common.page.response.PageResponse;
import com.capstone.backend.extracurricular.domain.entity.Extracurricular;
import com.capstone.backend.extracurricular.dto.response.ExtracurricularResponse;
import com.capstone.backend.extracurricular.facade.ExtracurricularFacade;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.ScheduleService;
import com.capstone.backend.member.domain.value.AcademicStatus;
import com.capstone.backend.member.domain.value.Role;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class MyExtracurricularFacadeTest {
    @Mock
    private MemberService memberService;
    @Mock
    private ScheduleService scheduleService;
    @InjectMocks
    private MyExtracurricularFacade myExtracurricularFacade;

    private Member member;
    private CustomUserDetails customUserDetails;
    @BeforeEach
    void setUp() {
        String email = "test@test.com";
        Role role = Role.ROLE_TEMPORARY_MEMBER;
        member = Member.builder()
                .id(1L)
                .email(email)
                .grade(1L)
                .name("김철수")
                .academicStatus(AcademicStatus.ENROLLED)
                .department("컴퓨터공학부")
                .college("공과대학")
                .email("abc@test.com")
                .role(role)
                .build();
        customUserDetails = Mockito.mock(CustomUserDetails.class);
        when(customUserDetails.getUsername()).thenReturn(member.getEmail());
        when(memberService.getByEmail(member.getEmail())).thenReturn(member);
    }

    @DisplayName("내가 추가한 비교과 조회")
    @Test
    void lookupMyExtracurricular_success() {
        // given
        int page = 0, size = 2;
        Long memberId = 1L;

        PageRequest pageRequest = PageRequest.of(page, size);

        Extracurricular e1 = Extracurricular.builder().id(1L).title("자바 스터디").build();
        Extracurricular e2 = Extracurricular.builder().id(2L).title("알고리즘 세미나").build();

        Page<Extracurricular> mockPage =
                new PageImpl<>(List.of(e1, e2), pageRequest, 5);

        when(scheduleService.getMyExtracurricular(memberId, pageRequest))
                .thenReturn(mockPage);

        // when
        PageResponse<ExtracurricularResponse> resp =
                myExtracurricularFacade.lookupMyExtracurricular(customUserDetails, page, size);

        // then
        verify(memberService).getByEmail(member.getEmail());
        verify(scheduleService).getMyExtracurricular(memberId, pageRequest);
    }

    @DisplayName("내가 추가한 비교과 검색")
    @Test
    void searchMyExtracurricular_success() {
        // given
        int page = 0, size = 2;
        Long memberId = 1L;

        PageRequest pageRequest = PageRequest.of(page, size);

        Extracurricular e1 = Extracurricular.builder().id(1L).title("자바 스터디").build();
        Extracurricular e2 = Extracurricular.builder().id(2L).title("알고리즘 세미나").build();
        String key = "test";
        Page<Extracurricular> mockPage =
                new PageImpl<>(List.of(e1, e2), pageRequest, 5);

        when(scheduleService.searchMyExtracurricular(memberId, key, pageRequest))
                .thenReturn(mockPage);

        // when
        PageResponse<ExtracurricularResponse> resp =
                myExtracurricularFacade.searchMyExtracurricular(key, page, size, customUserDetails);

        // then
        verify(memberService).getByEmail(member.getEmail());
        verify(scheduleService).searchMyExtracurricular(memberId, key, pageRequest);
    }
}
