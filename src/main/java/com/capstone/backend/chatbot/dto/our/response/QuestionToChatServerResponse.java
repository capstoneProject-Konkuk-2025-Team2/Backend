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
        List<Program> sources
) {
        public static QuestionToChatServerResponse of(JsonNode data) {
                // answer
                String answer = data.path("answer").asText("");

                // sources
                List<Program> programs = new ArrayList<>();
                JsonNode arr = data.path("sources");
                if (arr.isArray()) {
                        for (JsonNode node : arr) {
                                long id = node.path("id").asLong(0L);
                                String title = node.path("title").asText("");
                                String url = node.path("url").asText("");
                                // id가 0이면 잘못된 항목일 수 있으니 필요시 필터링
                                if (id != 0L) {
                                        programs.add(new Program(id, title, url));
                                }
                        }
                }

                return new QuestionToChatServerResponse(answer, programs);
        }
}
