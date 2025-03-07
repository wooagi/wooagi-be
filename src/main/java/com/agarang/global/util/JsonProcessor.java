package com.agarang.global.util;

import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * packageName    : com.agarang.global.util<br>
 * fileName       : JsonProcessor.java<br>
 * author         : okeio<br>
 * date           : 2025-02-13<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-13          okeio           최초생성<br>
 * 2025-02-14          okeio           숫자 변환 메서드 추가<br>
 * <br>
 */

@Component
public class JsonProcessor {

    private final ObjectMapper objectMapper;

    public JsonProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * JSON 코드 블록을 감싸고 있는 Markdown 스타일의 포맷을 제거하고 순수한 JSON 문자열을 추출합니다.
     *
     * <p>예제 입력:</p>
     * <pre>
     * ```json
     * { "name": "John", "age": "30" }
     * ```
     * </pre>
     *
     * <p>예제 출력:</p>
     * <pre>
     * { "name": "John", "age": "30" }
     * </pre>
     *
     * @param response JSON이 포함된 문자열
     * @return 정제된 JSON 문자열
     */
    public String extractJson(String response) {
        Pattern pattern = Pattern.compile("```json\\s*(\\{.*})\\s*```?", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return response;
    }

    /**
     * JSON 객체에서 null 값을 가진 필드를 제거합니다.
     *
     * @param node 입력 JSON 노드
     * @return null 필드가 제거된 JSON 노드
     */
    public JsonNode removeNullFields(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if (entry.getValue().isNull()) {
                    fields.remove();
                }
            }
            return objectNode;
        }
        return node;
    }

    /**
     * 문자열 형태의 JSON 데이터를 Jackson의 {@link JsonNode} 객체로 변환합니다.
     * 또한, 문자열로 저장된 숫자 값을 실제 숫자 타입(Integer 또는 Double)으로 변환합니다.
     *
     * @param jsonResponse JSON 형식의 문자열
     * @return 파싱된 JSON 노드
     * @throws BusinessException JSON 형식이 올바르지 않을 경우 예외 발생
     */
    public JsonNode parseJson(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            return convertStringNumbersToActualNumbers(rootNode);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_JSON_FORMAT);
        }
    }

    /**
     * JSON 객체에서 문자열로 저장된 숫자를 실제 숫자로 변환합니다.
     * <ul>
     *     <li>정수 형태의 문자열은 Integer로 변환</li>
     *     <li>실수 형태의 문자열은 Double로 변환</li>
     * </ul>
     *
     * <p>예제 입력:</p>
     * <pre>
     * { "age": "30", "height": "175.5" }
     * </pre>
     *
     * <p>예제 출력:</p>
     * <pre>
     * { "age": 30, "height": 175.5 }
     * </pre>
     *
     * @param node JSON 노드 객체
     * @return 변환된 JSON 노드
     */
    private JsonNode convertStringNumbersToActualNumbers(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode value = entry.getValue();

                if (value.isTextual()) {
                    String textValue = value.asText();
                    if (textValue.matches("-?\\d+")) { // 정수 변환 가능
                        objectNode.put(entry.getKey(), Integer.parseInt(textValue));
                    } else if (textValue.matches("-?\\d+\\.\\d+")) { // 실수 변환 가능
                        objectNode.put(entry.getKey(), Double.parseDouble(textValue));
                    }
                } else if (value.isObject() || value.isArray()) {
                    convertStringNumbersToActualNumbers(value); // 재귀 변환
                }
            }
            return objectNode;
        }
        return node;
    }
}
