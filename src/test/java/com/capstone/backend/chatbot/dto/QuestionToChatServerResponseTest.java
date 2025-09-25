package com.capstone.backend.chatbot.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @DisplayName("파싱 테스트 - 정상 케이스")
    @Test
    void of_ok() throws JsonProcessingException {
        String json = """
    {
        "answer": "문장1\\n문장2\\n",
        "sources": [
          { "id": 43, "title": "A", "url": "https://a" },
          { "id": 55, "title": "B", "url": "https://b" }
        ]
    }
    """;
        JsonNode root = objectMapper.readTree(json);

        QuestionToChatServerResponse res = QuestionToChatServerResponse.of(root);

        assertEquals("문장1\n문장2\n", res.answer());
        assertEquals(2, res.sources().size());
        assertEquals(43L, res.sources().get(0).id());
        assertEquals("A", res.sources().get(0).title());
        assertEquals("https://a", res.sources().get(0).url());
    }

    @DisplayName("파싱 테스트 - sources가 빈 배열일 때")
    @Test
    void of_emptySources() throws JsonProcessingException {
        String json = """
                {
                    "answer": "문장1\\n문장2\\n",
                    "sources": [
                    ]
                }
    """;
        JsonNode root = objectMapper.readTree(json);

        QuestionToChatServerResponse res = QuestionToChatServerResponse.of(root);

        assertEquals("문장1\n문장2\n", res.answer());
        assertNotNull(res.sources());
        assertTrue(res.sources().isEmpty());
    }
}
