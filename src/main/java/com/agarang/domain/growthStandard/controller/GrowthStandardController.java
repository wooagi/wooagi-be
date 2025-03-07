package com.agarang.domain.growthStandard.controller;

import com.agarang.domain.growthStandard.dto.response.DetailGrowthStandardResponse;
import com.agarang.domain.growthStandard.dto.response.GrowthStandardResponse;
import com.agarang.domain.growthStandard.service.GrowthStandardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.agarang.domain.growthStandard.controller<br>
 * fileName       : GrowthStandardController.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 24.<br>
 * description    : 성장 발달 컨트롤러입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24           okeio           최초생성<br>
 * <br>
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/growth-standards/")
@Tag(name = "성장 발달 API", description = "월령별 성장 발달 관련 정보를 제공하는 API")
public class GrowthStandardController {

    private final GrowthStandardService growthStandardService;

    /**
     * 발달 정보 요약 조회
     *
     * <p>
     * 특정 아기의 월령별 성장 발달 요약 정보를 조회하여 반환합니다.
     * </p>
     *
     * @param babyId 아기 ID (1 이상)
     * @return {@link GrowthStandardResponse} 성장 발달 요약 정보
     */
    @Operation(summary = "발달 정보 요약 조회", description = "요약된 월령별 성장 발달 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성장 발달 요약 정보 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GrowthStandardResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (babyId가 1 이상이어야 함)",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "해당 아기 정보 없음",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/baby-summary")
    public ResponseEntity<GrowthStandardResponse> getSummaryGrowthData(
            @RequestParam @Min(1)
            @Parameter(description = "아기 ID", example = "1")
            Integer babyId) {
        GrowthStandardResponse response = growthStandardService.getSummaryGrowthData(babyId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 발달 정보 상세 조회
     *
     * <p>
     * 특정 아기의 월령별 성장 발달 정보를 상세 조회하여 반환합니다.
     * </p>
     *
     * @param babyId 아기 ID (1 이상)
     * @return {@link DetailGrowthStandardResponse} 성장 발달 상세 정보
     */
    @Operation(summary = "발달 정보 상세 조회", description = "월령별 성장 발달 정보를 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성장 발달 상세 정보 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetailGrowthStandardResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (babyId가 1 이상이어야 함)",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "해당 아기 정보 없음",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/baby-detail")
    public ResponseEntity<DetailGrowthStandardResponse> getDetailGrowthData(
            @RequestParam @Min(1)
            @Parameter(description = "아기 ID", example = "1")
            Integer babyId) {
        DetailGrowthStandardResponse response = growthStandardService.getDetailGrowthData(babyId);
        return ResponseEntity.ok().body(response);
    }
}
