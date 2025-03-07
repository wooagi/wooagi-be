package com.agarang.domain.diary.service;

import com.agarang.global.ai.OpenAiService;
import com.agarang.global.util.HtmlProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * packageName    : com.agarang.domain.diary.service<br>
 * fileName       : KeywordService.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 13.<br>
 * description    :  Diary 엔티티의 keyword 관리하는 service 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.13          Fiat_lux           최초생성<br>
 */
@Service
@RequiredArgsConstructor
public class KeywordService {

    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    /**
     * 주어진 일기 텍스트에서 핵심 키워드를 추출합니다.
     *
     * <p>이 메서드는 HTML 형식의 일기 내용을 일반 텍스트로 변환한 후, OpenAI API를 사용하여
     * 1개에서 최대 6개의 핵심 키워드를 추출합니다. 추출된 키워드는 JSON 배열로 반환됩니다.</p>
     *
     * <h3>키워드 추출 방식:</h3>
     * <ul>
     *     <li>문장의 길이나 내용에 따라 1~6개의 키워드를 선택</li>
     *     <li>명사 중심으로 선정하되, 중요한 동사나 형용사도 포함 가능</li>
     *     <li>감정, 활동, 장소, 인물 등의 요소를 고려하여 균형 잡힌 키워드 선정</li>
     *     <li>링크(URL) 및 불필요한 내용은 포함하지 않음</li>
     * </ul>
     *
     * <h3>예시 입력:</h3>
     * <pre>
     *     "오늘은 날씨가 좋아서 공원에서 산책을 했다. 상쾌한 바람이 기분을 좋게 만들었다."
     * </pre>
     *
     * <h3>예시 출력:</h3>
     * <pre>
     *     ["날씨", "공원", "산책", "바람", "기분"]
     * </pre>
     *
     * @param diaryText 키워드를 추출할 일기 텍스트 (HTML 형식 포함 가능)
     * @return 추출된 키워드 목록 (최대 6개)
     */
    public List<String> extractKeywords(String diaryText) {

        String plainText = HtmlProcessor.convertHtmlToPlainText(diaryText);
        String promptText = """
                다음은 사용자가 작성한 일기입니다.
                                
                ---
                %s
                ---
                                
                이 일기의 주요 내용을 반영하는 핵심 키워드를 1개에서 최대 6개까지 JSON 배열로 출력해주세요.
                * 일기에서 핵심 키워드를 추출할 때, 문장의 길이나 내용에 따라 적절한 수의 키워드를 선택하는 것이 중요합니다. 만약 문장이 짧거나 내용이 간단하다면, 1개에서 3개 정도의 키워드로도 충분할 수 있습니다. 반대로 문장이 길면 3개에서 6개 정도의 키워드가 필요합니다.
                                
                * 키워드는 명사 중심으로 선정하되, 중요한 동사나 형용사도 포함할 수 있습니다.
                * 내용 분석: 일기의 주제를 파악하고, 그에 맞는 키워드를 선정합니다.
                * 이미지 URL 또는 링크 주소는 포함하지 마세요.
                * 중요성 평가: 각 키워드의 중요성을 평가하여, 가장 핵심적인 것들만 선택합니다.
                * 다양성 고려: 감정, 활동, 장소, 인물 등 다양한 측면을 반영하여 균형 잡힌 키워드를 선택합니다.
                * 이러한 방법을 통해 키워드 수를 조절할 수 있습니다. 예를 들어, 짧은 일기라면 다음과 같이 JSON 배열을 작성할 수 있습니다:
                * 결과는 **반드시** JSON 배열 형식으로 출력해야 합니다.
                                
                예시:
                ["기분", "사랑", "산책"]
                """.formatted(plainText);

        String responseJson = openAiService.sendPrompt(promptText).block();

        if (Objects.nonNull(responseJson)) {
            responseJson = responseJson.trim();

            responseJson = responseJson.replaceAll("(?i)^json\\s*", "");

            while (!responseJson.startsWith("[") && !responseJson.startsWith("{") && !responseJson.isEmpty()) {
                responseJson = responseJson.substring(1).trim();
            }
        }

        try {

            return objectMapper.readValue(responseJson, List.class);
        } catch (Exception e) {
            return List.of();
        }
    }


}
