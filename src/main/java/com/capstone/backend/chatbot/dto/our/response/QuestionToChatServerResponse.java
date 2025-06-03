package com.capstone.backend.chatbot.dto.our.response;

import com.capstone.backend.chatbot.dto.our.Program;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

public record QuestionToChatServerResponse(
        @Schema(description = "응답", example = "OO님의 관심사는 ~~이며, 다음의 비교과를 추천합니다.")
        String answer,
        @Schema(description = "추천할 비교과 리스트")
        List<Program> recommendedProgramList
) {
        public static QuestionToChatServerResponse of(
                JsonNode data
        ) {
                String userSummary = data.get("user_summary").asText("");
                String recommendationIntro = data.get("recommendation_intro").asText("");
                String answer = userSummary + "\n" + recommendationIntro;

                List<Program> recommendedPrograms = new ArrayList<>();
                JsonNode programArray = data.get("recommended_programs");
                if (programArray != null && programArray.isArray()) {
                        for (JsonNode node : programArray) {
                                String programText = node.get("text").asText("");
                                Program program = Program.of(programText);  // 위에서 정의한 정적 팩토리 메서드
                                recommendedPrograms.add(program);
                        }
                }

                return new QuestionToChatServerResponse(answer, recommendedPrograms);
        }
}
