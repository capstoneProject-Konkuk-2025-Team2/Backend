package com.capstone.backend.chatbot.dto.our;

import com.capstone.backend.chatbot.dto.our.parser.ProgramContentParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public record Program(
        String title,
        String url,
        String applicationPeriod,
        String targetAudience,
        String selectionMethod,
        String duration,
        String purposeOfTheActivity,
        String participationBenefitsAndExpectedOutcomes,
        String process,
        String modeOfOperation
) {

    private static Pair<Integer, Boolean> isSkip(String line, int index) {
        for(int i=index+1; i<ProgramContentParser.PARSING_CONTENT_KEYS.size(); i++) {
            if(line.startsWith(ProgramContentParser.PARSING_CONTENT_KEYS.get(i))){
                return Pair.of(i-index, true);
            }
        }
        return Pair.of(0, false);
    }
    public static Program of(String data) {
        String[] lines = data.split("\n");
        List<String> contents = new ArrayList<>();
        int i = -1;
        for (String line : lines) {
            if(i == -1) {
                i++;
                continue;
            }
            line = line.trim();
            String parsingKey = ProgramContentParser.PARSING_CONTENT_KEYS.get(i);
            if(line.startsWith(parsingKey)){
                contents.add(line.substring(parsingKey.length()).trim());
            }
            else {
                Pair<Integer, Boolean> skipInfo = isSkip(line, i);
                if(skipInfo.getRight()){
                    for(int j=0; j<skipInfo.getLeft(); j++) {
                        contents.add("");
                    }
                    i+=skipInfo.getLeft();
                    contents.add(line.substring(ProgramContentParser.PARSING_CONTENT_KEYS.get(i).length()).trim());
                    i++;
                    continue;
                }
                else {
                    i--;
                    String update = contents.get(i);
                    update += "\n";
                    update += line.trim();
                    contents.set(i, update);
                }
            }
            i++;
        }
        return new Program(
                contents.get(0),
                contents.get(1),
                contents.get(2),
                contents.get(3),
                contents.get(4),
                contents.get(5),
                contents.get(6),
                contents.get(7),
                contents.get(8),
                contents.get(9)
        );
    }
}
