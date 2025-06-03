package com.capstone.backend.chatbot.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capstone.backend.chatbot.dto.our.Program;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProgramTest {
    @DisplayName("파싱 테스트 - 모든 데이터가 들어왔을 때")
    @Test
    void of_all() {
        String data = "Unnamed: 0: 8\n제목: [혁신사업] 2025학년도 내:일 탐색 워크숍 5월-2차 (5/29 목): AI 시대 준비 전략: ChatGPT와 데이터 사이언스 (1학년 대상)\nURL: https://wein.konkuk.ac.kr/ptfol/imng/comprSbjtMngt/icmpNsbjtApl/genl/findTotPcondInfo.do?encSinbSeq=41dbcfc0d53d7838ef277e760912aa2a&intlNsbjtSxnCd=\n신청기간: 2025.05.02~2025.05.19\n대상자: 1학년\n선정방법: 선발\n진행기간: 2025.05.29 15:00~2025.05.29 17:00\n활동목적: 학업 성취 능력 향상 및 학습의 질 향상 도모\n학습역량, 창의역량, 소통역량 등 학업에 필요한 역량 향상을 위한 프로그램\n참여혜택 및 기대효과: 1. 비교과 수료증\n2. KUM마일리지 20점\n(만족도 조사 및 개선의견 설문조사를 완료해야 KUM마일리지 적립이 완료됩니다. \n재학생 대상 비교과 프로그램이므로, 휴학생의 경우 참여 및 수료는 가능하나 마일리지 적립이 별도로 되지 않습니다)\n진행절차: 위인전 신청→선정자 개별 문자 발송(5/21(수))→참여→수료 예정자 만족도설문→최종 수료 및 KUM마일리지 적립\n운영방식: 대면(경영관 101호)";
        Program program = Program.of(data);
        assertEquals("[혁신사업] 2025학년도 내:일 탐색 워크숍 5월-2차 (5/29 목): AI 시대 준비 전략: ChatGPT와 데이터 사이언스 (1학년 대상)", program.title());
        assertEquals("https://wein.konkuk.ac.kr/ptfol/imng/comprSbjtMngt/icmpNsbjtApl/genl/findTotPcondInfo.do?encSinbSeq=41dbcfc0d53d7838ef277e760912aa2a&intlNsbjtSxnCd=", program.url());
        assertEquals("2025.05.02~2025.05.19", program.applicationPeriod());
        assertEquals("1학년", program.targetAudience());
        assertEquals("선발", program.selectionMethod());
        assertEquals("2025.05.29 15:00~2025.05.29 17:00", program.duration());
        assertEquals("학업 성취 능력 향상 및 학습의 질 향상 도모\n학습역량, 창의역량, 소통역량 등 학업에 필요한 역량 향상을 위한 프로그램", program.purposeOfTheActivity());
        assertEquals("1. 비교과 수료증\n2. KUM마일리지 20점\n(만족도 조사 및 개선의견 설문조사를 완료해야 KUM마일리지 적립이 완료됩니다.\n재학생 대상 비교과 프로그램이므로, 휴학생의 경우 참여 및 수료는 가능하나 마일리지 적립이 별도로 되지 않습니다)", program.participationBenefitsAndExpectedOutcomes());
        assertEquals("위인전 신청→선정자 개별 문자 발송(5/21(수))→참여→수료 예정자 만족도설문→최종 수료 및 KUM마일리지 적립", program.process());
        assertEquals("대면(경영관 101호)", program.modeOfOperation());
    }

    @DisplayName("파싱 테스트 - 일부 데이터만 들어왔을 때")
    @Test
    void of_sub() {
        String data = "Unnamed: 0: 18\n제목: [혁신사업] [직무의 발견] 코멘토 채용트렌드 특강_5/26 16시\nURL: https://wein.konkuk.ac.kr/ptfol/imng/comprSbjtMngt/icmpNsbjtApl/emplym/findTotPcondInfo.do?encSinbSeq=759b92b30e54e591edd0ed718ef54409&intlNsbjtSxnCd=\n신청기간: 2025.05.12~2025.05.25\n대상자: 1학년 2학년 3학년 4학년\n선정방법: 선착순\n진행기간: 2025.05.26 16:00~2025.05.26 18:00\n활동목적: AI시대의 취준 전략과 채용 트렌드\n진행절차: 참가신청 링크: https://comento.typeform.com/to/dr7bSGmh\n운영방식: 온라인 / 업체 운영";
        Program program = Program.of(data);
        assertEquals("[혁신사업] [직무의 발견] 코멘토 채용트렌드 특강_5/26 16시", program.title());
        assertEquals("https://wein.konkuk.ac.kr/ptfol/imng/comprSbjtMngt/icmpNsbjtApl/emplym/findTotPcondInfo.do?encSinbSeq=759b92b30e54e591edd0ed718ef54409&intlNsbjtSxnCd=", program.url());
        assertEquals("2025.05.12~2025.05.25", program.applicationPeriod());
        assertEquals("1학년 2학년 3학년 4학년", program.targetAudience());
        assertEquals("선착순", program.selectionMethod());
        assertEquals("2025.05.26 16:00~2025.05.26 18:00", program.duration());
        assertEquals("AI시대의 취준 전략과 채용 트렌드", program.purposeOfTheActivity());
        assertEquals("", program.participationBenefitsAndExpectedOutcomes());
        assertEquals("참가신청 링크: https://comento.typeform.com/to/dr7bSGmh", program.process());
        assertEquals("온라인 / 업체 운영", program.modeOfOperation());
    }
}
