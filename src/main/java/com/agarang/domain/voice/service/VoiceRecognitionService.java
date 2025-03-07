package com.agarang.domain.voice.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.record.entity.type.FormulaFeeding;
import com.agarang.domain.record.entity.type.NormalFeeding;
import com.agarang.domain.record.entity.type.Pumping;
import com.agarang.domain.record.entity.type.PumpingFeeding;
import com.agarang.domain.record.repository.type.FormulaFeedingRepository;
import com.agarang.domain.record.repository.type.NormalFeedingRepository;
import com.agarang.domain.record.repository.type.PumpingFeedingRepository;
import com.agarang.domain.record.repository.type.PumpingRepository;
import com.agarang.domain.voice.dto.request.ClassificationRequest;
import com.agarang.global.ai.OpenAiService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.agarang.global.util.JsonProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


/**
 * packageName    : com.agarang.domain.voice.service<br>
 * fileName       : VoiceRecognitionService.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 14.<br>
 * description    : 음성 인식 및 텍스트 분류 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.14          okeio           최초생성<br>
 * 25.02.19          okeio           openai api 호출 로직 변경<br>
 * <br>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceRecognitionService {
    private final JsonProcessor jsonProcessor;
    private final OpenAiService openAiService;
    private final BabyRepository babyRepository;
    private final PumpingRepository pumpingRepository;
    private final NormalFeedingRepository normalFeedingRepository;
    private final FormulaFeedingRepository formulaFeedingRepository;
    private final PumpingFeedingRepository pumpingFeedingRepository;

    /**
     * 음성 인식된 텍스트를 기반으로 카테고리를 분류하고 JSON 형식으로 반환합니다.
     *
     * <p>
     * 이 메서드는 OpenAI API를 호출하여 음성 인식된 텍스트를 특정 카테고리로 분류한 후,
     * 분류된 데이터를 JSON 형태로 반환합니다. 결과 JSON에서 null 필드를 제거하고,
     * 특정 카테고리(`GROWTH_STATUS`, `PUMPING`, `FEEDING`)의 경우 추가적인 데이터 처리를 수행합니다.
     * </p>
     *
     * @param classificationRequest 분류할 텍스트 요청 객체
     * @return 분류된 데이터를 포함한 {@link JsonNode} 객체
     */
    public JsonNode classifyTextUsingCompletion(ClassificationRequest classificationRequest) {
        String prompt = """
                You are a text classification model for Korean parenting records. Classify the text into the correct category and return the result in **snake_case JSON** format.
                
                         ### User's input:
                         %s
                
                         ### Current time:
                         %s
                
                         ### Categories:
                         - FEEDING
                         - PUMPING(유축)
                         - SOLID_FOOD
                         - BATH
                         - EXCRETION
                         - SLEEP
                         - HOSPITAL
                         - FEVER
                         - MEDICATION
                         - GROWTH_STATUS(발육)
                
                         ### JSON Format Rules:
                         1. **All JSON responses must follow snake_case for field names and UPPER_CASE for enum values.**
                         2. Always include `"is_classified"` in the output:
                            - `"is_classified": true` → `"record_type"` must be included.
                            - `"is_classified": false` → `"record_type"` must be **excluded**, return `{ "is_classified": false, "message": "not classified" }`.
                         3. Always categories include a started_at field, which represents the start time in the format yyyy-MM-ddThh:mm:ss.
                            If the user mentions a time for the record, set `started_at` based on the specified time. If no specific time is mentioned, reference the provided `Current time` to set `started_at`.
                            For the SLEEP category, an additional ended_at field should be included.
                            DO NOT generate your own timestamp.
                         4. Each category has its own set of allowed fields.
                            - If a field is missing in the input, set it to `null`.
                            - Do not include fields that do not belong to the category.
                         5. Example JSON output:
                        {
                          "is_classified": true,
                          "record_type": "FEEDING",
                          "feeding_type": "BREAST_FEEDING",
                          "position": "STARTLEFT",
                          "left_time": 10,
                          "right_time": 5,
                          "total_time": 15,
                          "started_at": "2025-02-06T11:51:00"
                        }
                         6. **Output must be a valid JSON object, without Markdown formatting or extra text.**
                         7. **Directly return the JSON object as cleanedJson format.**
                         8. Ensure that fields such as size, amount, and other numerical values are mapped as Numbers in JSON instead of Strings.
                
                         ### JSON Foramt Examples:
                
                         1. **FEEDING**
                            - feeding_type: BREAST_FEEDING, FORMULA_FEEDING, NORMAL_FEEDING(default), PUMPING_FEEDING(유축 수유)
                            - position(only for BREAST_FEEDING): LEFT, RIGHT, BOTH, STARTLEFT (left to right), STARTRIGHT (right to left), UNKNOWN
                            - left_time, right_time, total_time (only for BREAST_FEEDING, minute)
                            - amount (not applicable to BREAST_FEEDING, ml)
                
                         2. **EXCRETION**
                            - excretion_type: POOP, PEE(default), BOTH
                            - excretion_status (only for POOP or BOTH): SNOT, HARD, GOOD, DIARRHEA, BLOOD
                            - excretion_color (only for POOP or BOTH): LIGHT_BROWN, BROWN, DARK_BROWN, GREEN, GRAY, RED
                
                         3. **MEDICATION**
                            - medication_type: ANTIPYRETIC, OTHERS
                            - specific_type (only for ANTIPYRETIC): IBUPROFEN, ACETAMINOPHEN
                            - amount (only for ANTIPYRETIC, float in JSON with up to one decimal place)
                
                         4. **FEVER**
                            - position: EAR, ARMPIT, FOREHEAD
                            - temperature (Celsius, BigDecimal(2,1))
                
                         5. **HOSPITAL**
                            - visit_type: CHECKUP, DISEASE, VACCINATION, OTHERS
                
                         6. **PUMPING**
                            - position: LEFT, RIGHT, BOTH, UNKNOWN
                            - left_amount, right_amount, total_amount (ml)
                
                         7. **SOLID_FOOD**
                            - amount
                
                         8. **SLEEP**
                            - ended_at (LocalDateTime, format: `yyyy-MM-ddThh:mm:ss.sssss`)
                
                         9. **BATH**
                
                         10. **GROWTH_STATUS**
                            - growth_status_type(required): HEIGHT, WEIGHT, HEAD_SIZE(머리 둘레)
                            - size (cm, ml)
                """.formatted(classificationRequest.getText(), LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        String responseData = openAiService.sendPromptForVoiceRecognition(prompt).block();
        log.debug("responseData: {}", responseData);

        String cleanedJson = jsonProcessor.extractJson(responseData);
        JsonNode parsedJson = jsonProcessor.parseJson(cleanedJson);
        parsedJson = jsonProcessor.removeNullFields(parsedJson);

        if (parsedJson.has("record_type") && "GROWTH_STATUS".equals(parsedJson.get("record_type").asText())) {
            parsedJson = processGrowthStatus(parsedJson, classificationRequest.getBabyId());
        }
        if (parsedJson.has("record_type") && "PUMPING".equals(parsedJson.get("record_type").asText())) {
            parsedJson = processPumping(parsedJson, classificationRequest.getBabyId());
        }
        if (parsedJson.has("record_type") && "FEEDING".equals(parsedJson.get("record_type").asText())) {
            parsedJson = processFeeding(parsedJson, classificationRequest.getBabyId());
        }

        return parsedJson;
    }

    private JsonNode processGrowthStatus(JsonNode parsedJson, Integer babyId) {
        ObjectNode mutableJson = (ObjectNode) parsedJson;

        if (!mutableJson.hasNonNull("growth_status_type")) {
            throw new BusinessException(ErrorCode.INVALID_JSON_FORMAT);
        }

        if (mutableJson.hasNonNull("size"))
            return mutableJson;

        String growthStatusType = mutableJson.get("growth_status_type").asText();
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        switch (growthStatusType) {
            case "HEAD_SIZE":
                mutableJson.put("size", baby.getHeadSize());
                break;

            case "WEIGHT":
                mutableJson.put("size", baby.getWeight());
                break;

            case "HEIGHT":
                mutableJson.put("size", baby.getHeight());
                break;
        }
        return mutableJson;
    }

    private JsonNode processPumping(JsonNode parsedJson, Integer babyId) {
        ObjectNode mutableJson = (ObjectNode) parsedJson;

        if (mutableJson.hasNonNull("total_amount")) {
            return mutableJson;
        }
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);

        Integer totalAmount = pumpingRepository.getLatestPumpingByBabyAndStartedAtBetween(baby, startDate, endDate)
                .map(Pumping::getTotalAmount)
                .orElse(0);

        log.debug("최근 PUMPING totalAmount: {}", totalAmount);

        mutableJson.put("total_amount", totalAmount);
        return mutableJson;
    }

    private JsonNode processFeeding(JsonNode parsedJson, Integer babyId) {
        ObjectNode mutableJson = (ObjectNode) parsedJson;

        if (!mutableJson.hasNonNull("feeding_type")) {
            throw new BusinessException(ErrorCode.INVALID_JSON_FORMAT);
        }

        if (mutableJson.hasNonNull("amount")) {
            return mutableJson;
        }

        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        Integer amount = 0;

        switch (mutableJson.get("feeding_type").asText()) {
            case "PUMPING_FEEDING":
                amount = pumpingFeedingRepository
                        .getLatestPumpingFeedingByBabyAndStartedAtBetween(baby, startDate, endDate)
                        .map(PumpingFeeding::getAmount)
                        .orElse(0);
                log.debug("최근 PUMPING_FEEDING amount: {}", amount);
                mutableJson.put("amount", amount);
                break;
            case "NORMAL_FEEDING":
                amount = normalFeedingRepository
                        .getLatestNormalFeedingByBabyAndStartedAtBetween(baby, startDate, endDate)
                        .map(NormalFeeding::getAmount)
                        .orElse(0);
                log.debug("최근 NORMAL_FEEDING amount: {}", amount);
                mutableJson.put("amount", amount);
                break;
            case "FORMULA_FEEDING":
                amount = formulaFeedingRepository.getLatestFormulaFeedingByBabyAndStartedAtBetween(baby, startDate, endDate)
                        .map(FormulaFeeding::getAmount)
                        .orElse(0);
                log.debug("최근 FORMULA_FEEDING amount: {}", amount);
                mutableJson.put("amount", amount);
                break;
        }

        return mutableJson;
    }
}
