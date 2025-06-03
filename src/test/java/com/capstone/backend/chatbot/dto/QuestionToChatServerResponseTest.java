package com.capstone.backend.chatbot.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capstone.backend.chatbot.dto.our.response.QuestionToChatServerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QuestionToChatServerResponseTest {
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }
    @DisplayName("파싱 테스트")
    @Test
    void of() throws JsonProcessingException {
        //given
        String chatbotServerResponse = "{\n"
                + "    \"user_summary\": \"조은영님의 관심사는 AI, 데이터, 프로그래밍입니다. 시간표는 다음과 같습니다:\\n월 09:00~10:30\\n화 13:00~14:30\\n수 15:00~16:30\",\n"
                + "    \"recommendation_intro\": \"위 정보를 바탕으로 시간표와 겹치지 않고 AI, 데이터, 프로그래밍과 관련된 활동을 추천해드립니다.\",\n"
                + "    \"answer\": \"모델 서버 오류: [Errno -2] Name or service not known\",\n"
                + "    \"total_programs\": 11,\n"
                + "    \"recommended_programs\": [\n"
                + "      {\n"
                + "        \"text\": \"Unnamed: 0: 8\\n제목: [혁신사업] 2025학년도 내:일 탐색 워크숍 5월-2차 (5/29 목): AI 시대 준비 전략: ChatGPT와 데이터 사이언스 (1학년 대상)\\nURL: https://wein.konkuk.ac.kr/ptfol/imng/comprSbjtMngt/icmpNsbjtApl/genl/findTotPcondInfo.do?encSinbSeq=41dbcfc0d53d7838ef277e760912aa2a&intlNsbjtSxnCd=\\n신청기간: 2025.05.02~2025.05.19\\n대상자: 1학년\\n선정방법: 선발\\n진행기간: 2025.05.29 15:00~2025.05.29 17:00\\n활동목적: 학업 성취 능력 향상 및 학습의 질 향상 도모\\n학습역량, 창의역량, 소통역량 등 학업에 필요한 역량 향상을 위한 프로그램\\n참여혜택 및 기대효과: 1. 비교과 수료증\\n2. KUM마일리지 20점\\n(만족도 조사 및 개선의견 설문조사를 완료해야 KUM마일리지 적립이 완료됩니다. \\n재학생 대상 비교과 프로그램이므로, 휴학생의 경우 참여 및 수료는 가능하나 마일리지 적립이 별도로 되지 않습니다)\\n진행절차: 위인전 신청→선정자 개별 문자 발송(5/21(수))→참여→수료 예정자 만족도설문→최종 수료 및 KUM마일리지 적립\\n운영방식: 대면(경영관 101호)\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"text\": \"Unnamed: 0: 9\\n제목: [혁신사업] 2025학년도 내:일 탐색 워크숍 5월-1차 (5/27 화): AI 시대 준비 전략: ChatGPT와 데이터 사이언스 (1학년 대상)\\nURL: https://wein.konkuk.ac.kr/ptfol/imng/comprSbjtMngt/icmpNsbjtApl/genl/findTotPcondInfo.do?encSinbSeq=45d9221f9ed02e7690eb8aad7e9e4771&intlNsbjtSxnCd=\\n신청기간: 2025.05.02~2025.05.19\\n대상자: 1학년\\n선정방법: 선발\\n진행기간: 2025.05.27 15:00~2025.05.27 17:00\\n활동목적: 학업 성취 능력 향상 및 학습의 질 향상 도모\\n학습역량, 창의역량, 소통역량 등 학업에 필요한 역량 향상을 위한 프로그램\\n참여혜택 및 기대효과: 1. 비교과 수료증\\n2. KUM마일리지 20점\\n(만족도 조사 및 개선의견 설문조사를 완료해야 KUM마일리지 적립이 완료됩니다. \\n재학생 대상 비교과 프로그램이므로, 휴학생의 경우 참여 및 수료는 가능하나 마일리지 적립이 별도로 되지 않습니다)\\n진행절차: 위인전 신청→선정자 개별 문자 발송(5/21(수))→참여→수료 예정자 만족도설문→최종 수료 및 KUM마일리지 적립\\n운영방식: 대면(경영관 101호)\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"text\": \"Unnamed: 0: 18\\n제목: [혁신사업] [직무의 발견] 코멘토 채용트렌드 특강_5/26 16시\\nURL: https://wein.konkuk.ac.kr/ptfol/imng/comprSbjtMngt/icmpNsbjtApl/emplym/findTotPcondInfo.do?encSinbSeq=759b92b30e54e591edd0ed718ef54409&intlNsbjtSxnCd=\\n신청기간: 2025.05.12~2025.05.25\\n대상자: 1학년 2학년 3학년 4학년\\n선정방법: 선착순\\n진행기간: 2025.05.26 16:00~2025.05.26 18:00\\n활동목적: AI시대의 취준 전략과 채용 트렌드\\n진행절차: 참가신청 링크: https://comento.typeform.com/to/dr7bSGmh\\n운영방식: 온라인 / 업체 운영\"\n"
                + "      }\n"
                + "    ]\n"
                + "  }";
        JsonNode dataNode = objectMapper.readTree(chatbotServerResponse);
        //when
        QuestionToChatServerResponse response = QuestionToChatServerResponse.of(dataNode);
        //then
        assertEquals("조은영님의 관심사는 AI, 데이터, 프로그래밍입니다. 시간표는 다음과 같습니다:\n월 09:00~10:30\n화 13:00~14:30\n수 15:00~16:30\n위 정보를 바탕으로 시간표와 겹치지 않고 AI, 데이터, 프로그래밍과 관련된 활동을 추천해드립니다.", response.answer());
        assertEquals(3, response.recommendedProgramList().size());
    }
}
