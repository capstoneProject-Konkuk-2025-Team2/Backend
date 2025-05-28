package com.capstone.backend.chatbot.facade;

import com.capstone.backend.chatbot.dto.request.ChatbotRegisterRequest;
import com.capstone.backend.chatbot.dto.RegisterTimetable;
import com.capstone.backend.chatbot.service.ChatbotService;
import com.capstone.backend.core.auth.dto.CustomUserDetails;
import com.capstone.backend.member.domain.entity.Interest;
import com.capstone.backend.member.domain.entity.Member;
import com.capstone.backend.member.domain.entity.Timetable;
import com.capstone.backend.member.domain.service.InterestService;
import com.capstone.backend.member.domain.service.MemberService;
import com.capstone.backend.member.domain.service.TimetableService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatbotFacade {
    private final MemberService memberService;
    private final InterestService interestService;
    private final TimetableService timetableService;
    private final ChatbotService chatbotService;

    public Boolean registerMemberInfo(CustomUserDetails customUserDetails) {
        Member member = memberService.getByEmail(customUserDetails.getUsername());
        List<Interest> interestList = interestService.findAllByMemberId(member.getId());
        List<String> contents = interestList.stream()
                .map(Interest::getContent)
                .toList();
        List<Timetable> timetableList = timetableService.findAllByMemberId(member.getId());
        List<RegisterTimetable> registerTimetableList = timetableList.stream()
                .map(RegisterTimetable::of)
                .toList();
        ChatbotRegisterRequest chatbotRegisterRequest = ChatbotRegisterRequest.of(
                member,
                contents,
                registerTimetableList
        );
        chatbotService.register(chatbotRegisterRequest);
        return true;
    }
}
