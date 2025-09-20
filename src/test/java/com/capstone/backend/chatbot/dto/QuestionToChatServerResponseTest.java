package com.capstone.backend.chatbot.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capstone.backend.chatbot.dto.our.response.QuestionToChatServerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
                + "    \"answer\": \"1.[혁신 사업] 자기탐색 특강\\n 2.[대학일자리+] 릴레이 직무 컨설팅_SW/IT 데이터 - KUM마일리지:10점\\n\",\n"
                + "    \"recommendations\": [\n"
                + "      {\n"
                + "        \"extracurricularId\": 43,\n"
                + "        \"title\": \"[혁신 사업] 자기탐색 특강\",\n"
                + "        \"score\": 0.2588164143755251,\n"
                + "        \"question_similarity\": 0.2460454888129249,\n"
                + "        \"interest_similarity\": 0.309909371662952586,\n"
                + "        \"kum_mileage\": null\n"
                + "      },\n"
                + "      {\n"
                + "        \"extracurricularId\": 29,\n"
                + "        \"title\": \"[혁신 사업] 2025학년도 내:일 탐색 워크숍 5월-2차 (5/29 목)\",\n"
                + "        \"score\": 0.1870620189366573,\n"
                + "        \"question_similarity\": 0.2460454888129249,\n"
                + "        \"interest_similarity\": 0.309909371662952586,\n"
                + "        \"kum_mileage\": null\n"
                + "      }\n"
                + "    ],\n"
                + "    \"recommended_ids\" : [43, 29, 23, 50, 49]\n"
                + "}";
        JsonNode dataNode = objectMapper.readTree(chatbotServerResponse);
        //when
        QuestionToChatServerResponse response = QuestionToChatServerResponse.of(dataNode);
        //then
        assertEquals("1.[혁신 사업] 자기탐색 특강\n 2.[대학일자리+] 릴레이 직무 컨설팅_SW/IT 데이터 - KUM마일리지:10점\n", response.answer());
        // 기대되는 recommended_ids
        List<Long> expectedIds = List.of(43L, 29L, 23L, 50L, 49L);
        assertEquals(expectedIds.size(), response.recommendedProgramList().size());
        for (int i = 0; i < expectedIds.size(); i++) {
            assertEquals(expectedIds.get(i), response.recommendedProgramList().get(i));
        }
    }

    @DisplayName("파싱 테스트 - id 리스트가 null일 때도 정상동작 하는지")
    @Test
    void of_nullCase() throws JsonProcessingException {
        //given
        String chatbotServerResponse = "{\n"
                + "    \"answer\": \"1.[혁신 사업] 자기탐색 특강\\n 2.[대학일자리+] 릴레이 직무 컨설팅_SW/IT 데이터 - KUM마일리지:10점\\n\",\n"
                + "    \"recommendations\": [\n"
                + "      {\n"
                + "        \"extracurricularId\": 43,\n"
                + "        \"title\": \"[혁신 사업] 자기탐색 특강\",\n"
                + "        \"score\": 0.2588164143755251,\n"
                + "        \"question_similarity\": 0.2460454888129249,\n"
                + "        \"interest_similarity\": 0.309909371662952586,\n"
                + "        \"kum_mileage\": null\n"
                + "      },\n"
                + "      {\n"
                + "        \"extracurricularId\": 29,\n"
                + "        \"title\": \"[혁신 사업] 2025학년도 내:일 탐색 워크숍 5월-2차 (5/29 목)\",\n"
                + "        \"score\": 0.1870620189366573,\n"
                + "        \"question_similarity\": 0.2460454888129249,\n"
                + "        \"interest_similarity\": 0.309909371662952586,\n"
                + "        \"kum_mileage\": null\n"
                + "      }\n"
                + "    ],\n"
                + "    \"recommended_ids\" : []\n"
                + "}";
        JsonNode dataNode = objectMapper.readTree(chatbotServerResponse);
        //when
        QuestionToChatServerResponse response = QuestionToChatServerResponse.of(dataNode);
        //then
        assertEquals("1.[혁신 사업] 자기탐색 특강\n 2.[대학일자리+] 릴레이 직무 컨설팅_SW/IT 데이터 - KUM마일리지:10점\n", response.answer());
        // 기대되는 recommended_ids
        List<Long> expectedIds = List.of();
        assertEquals(0, response.recommendedProgramList().size());
    }
}
