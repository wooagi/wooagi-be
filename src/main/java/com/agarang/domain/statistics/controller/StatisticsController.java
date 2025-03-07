package com.agarang.domain.statistics.controller;

import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.statistics.dto.response.GrowthHistoryResponse;
import com.agarang.domain.statistics.dto.response.GrowthStatisticsResponse;
import com.agarang.domain.statistics.dto.response.WeeklyGetResponse;
import com.agarang.domain.statistics.service.GrowthStatisticsService;
import com.agarang.domain.statistics.service.WeeklyStatisticsManager;
import com.agarang.domain.statistics.service.WeeklyStatisticsService;
import com.agarang.domain.user.dto.CustomUserDetails;
import com.agarang.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * packageName    : com.agarang.domain.statistics.controller<br>
 * fileName       : StatisticsController.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-04<br>
 * description    : 통계 controller 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-04          nature1216          최초생성<br>
 * <br>
 */

@Tag(name = "통계", description = "통계 관리 API")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorCode.class)))
        }
)
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final GrowthStatisticsService growthStatisticsService;
    private final WeeklyStatisticsManager weeklyStatisticsManager;

    /**
     * 특정 아기의 발육 상태 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 특정 발육 상태 타입에 해당하는 기록을 조회하고,
     * 변환된 {@link GrowthHistoryResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param babyId      발육 상태 기록을 조회할 아기의 ID
     * @param type        조회할 발육 상태 타입
     * @return 발육 상태 기록 목록을 포함한 {@link GrowthHistoryResponse} 객체
     */
    @Operation(summary = "발육상태 기록 조회", responses = {@ApiResponse(responseCode = "200")}, description = "조회 성공")
    @GetMapping("/growth-history")
    public ResponseEntity<GrowthHistoryResponse> getGrowthHistory(@Parameter(description = "회원 인증 정보", required = true)
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @Parameter(description = "아기 id", required = true)
                                                                  @RequestParam("baby_id") Integer babyId,
                                                                  @Parameter(description = "발육상태 타입", required = true)
                                                                  @RequestParam("type")GrowthStatusType type) {
        GrowthHistoryResponse response =
                growthStatisticsService.getGrowthHistory(userDetails.getUserId(), babyId, type);

        return ResponseEntity.ok(response);
    }

    /**
     * 특정 발육 상태 기록의 통계를 계산합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 기록의 발육 통계를 계산하고,
     * 변환된 {@link GrowthStatisticsResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param recordId    통계를 조회할 기록의 ID
     * @return 해당 기록의 발육 상태 통계를 포함한 {@link GrowthStatisticsResponse} 객체
     */
    @Operation(summary = "발육상태 통계 계산", responses = {@ApiResponse(responseCode = "200")}, description = "계산 성공")
    @GetMapping("/growth/{recordId}")
    public ResponseEntity<GrowthStatisticsResponse> getGrowthStatistics(@Parameter(description = "회원 인증 정보", required = true)
                                                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @Parameter(description = "기록 id", required = true)
                                                                        @PathVariable("recordId") Integer recordId) {
        GrowthStatisticsResponse response = growthStatisticsService.getStatistics(userDetails.getUserId(), recordId);

        return ResponseEntity.ok(response);
    }

    /**
     * 특정 아기의 주간 통계를 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 특정 기록 유형(수유, 수면, 배변)에 대한
     * 주간 통계를 계산하고, 변환된 {@link WeeklyGetResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param babyId      주간 통계를 조회할 아기의 ID
     * @param type        기록 유형(수유, 수면, 배변 중 하나)
     * @param date        기준일자
     * @return 주간 통계를 포함한 {@link WeeklyGetResponse} 객체
     */
    @Operation(summary = "주간통계 조회", responses = {@ApiResponse(responseCode = "200")}, description = "조회 성공")
    @GetMapping("/weekly/{babyId}")
    public ResponseEntity<WeeklyGetResponse> getWeeklyStatistics(@Parameter(description = "회원 인증 정보", required = true)
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                             @Parameter(description = "아기 id", required = true)
                                                                             @PathVariable("babyId") Integer babyId,
                                                                             @Parameter(description = "기록 카테고리(수유, 수면, 배변 한정)", required = true)
                                                                             @RequestParam("type") RecordType type,
                                                                             @Parameter(description = "기준일자", required = true)
                                                                             @RequestParam("date") LocalDate date) {
        WeeklyGetResponse response = weeklyStatisticsManager.getWeeklyStatistics(type, date, userDetails.getUserId(), babyId);

        return ResponseEntity.ok(response);
    }
}
