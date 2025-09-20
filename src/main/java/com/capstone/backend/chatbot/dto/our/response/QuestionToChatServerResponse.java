package com.capstone.backend.chatbot.dto.our.response;

import com.capstone.backend.chatbot.dto.our.Program;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

public record QuestionToChatServerResponse(
        @Schema(description = "응답", example = "OO님의 관심사는 ~~이며, 다음의 비교과를 추천합니다.")
        String answer,
        @Schema(description = "추천할 비교과 id 목록")
        List<Long> recommendedProgramList
) {
        public static QuestionToChatServerResponse of(
                JsonNode data
        ) {
                String answer = data.get("answer").asText("");

                JsonNode programArray = data.get("recommended_ids");
                List<Long> programIdsList = new ArrayList<>();
                if (programArray != null && programArray.isArray()) {
                        for (JsonNode node : programArray) {
                                long programId = node.asLong(); // 숫자로 꺼내기
                                programIdsList.add(programId);
                        }
                }

                return new QuestionToChatServerResponse(answer, programIdsList);
        }
}
