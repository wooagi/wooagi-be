package com.agarang.domain.voice.controller;

import com.agarang.domain.voice.dto.request.ClassificationRequest;
import com.agarang.domain.voice.service.VoiceRecognitionService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.agarang.domain.speechRecognition.controller<br>
 * fileName       : VoiceRecognitionController.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 13.<br>
 * description    : 음성 인식 컨트롤러입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.13          okeio           최초생성<br>
 * 25.02.13          okeio           최초생성<br>
 * 25.02.19          okeio           classifyText 반환 타입 변경<br>
 * <br>
 */
@RestController
@RequestMapping("/api/voice")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "음성인식 API", description = "음성 인식 된 텍스트를 기반으로 기록 카테고리르 분류하는 API")
public class VoiceRecognitionController {
    private final VoiceRecognitionService voiceRecognitionService;

    /**
     * 텍스트 분류
     *
     * <p>
     * 이 메서드는 음성 인식된 텍스트를 카테고리로 분류하여 JSON 형식으로 반환합니다.
     * </p>
     *
     * @param classificationRequest 분류할 텍스트 요청 객체
     * @return JSON 형식의 분류 결과를 포함한 {@link ResponseEntity}
     */
    @Operation(summary = "텍스트 분류", description = "음성 인식된 텍스트를 기록 카테고리로 분류합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "분류 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonNode.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/classify")
    public ResponseEntity<JsonNode> classifyText(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "분류할 텍스트 요청 객체")
            @RequestBody ClassificationRequest classificationRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(voiceRecognitionService.classifyTextUsingCompletion(classificationRequest));
    }
}
