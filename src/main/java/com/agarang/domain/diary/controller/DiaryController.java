package com.agarang.domain.diary.controller;

import com.agarang.domain.diary.dto.request.DiaryRegisterRequest;
import com.agarang.domain.diary.dto.request.DiaryUpdateRequest;
import com.agarang.domain.diary.dto.response.DiaryEmojiResponse;
import com.agarang.domain.diary.dto.response.DiaryGetResponse;
import com.agarang.domain.diary.dto.response.DiaryListResponse;
import com.agarang.domain.diary.dto.response.DiaryResponse;
import com.agarang.domain.diary.service.DiaryService;
import com.agarang.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

/**
 * packageName    : com.agarang.domain.diary.controller<br>
 * fileName       : DiaryController.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 24.<br>
 * description    :  Diary entity 의 controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          Fiat_lux            최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
@Tag(name = "다이어리 API", description = "다이어리 관련 기능을 제공합니다.")
public class DiaryController {
    private final DiaryService diaryService;

    /**
     * 새로운 다이어리를 등록합니다.
     *
     * @param customUserDetails    인증된 사용자 정보
     * @param diaryRegisterRequest 다이어리 등록 요청 정보
     * @param babyId               다이어리를 등록할 아기의 ID
     * @return 등록된 다이어리 정보 {@link DiaryResponse}
     */
    @Operation(summary = "다이어리 등록", description = "주 양육자가 특정 아기의 다이어리를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "다이어리 등록 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "다이어리 등록 권한이 없는 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/babies/{babyId}")
    public ResponseEntity<DiaryResponse> registerDiary(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                       @Parameter(description = "다이어리를 등록할 아기의 ID", required = true) @PathVariable Integer babyId,
                                                       @RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                               schema = @Schema(implementation = DiaryRegisterRequest.class)
                                                       )) DiaryRegisterRequest diaryRegisterRequest) {

        int userId = customUserDetails.getUserId();
        DiaryResponse diaryResponse = diaryService.registerDiary(userId, babyId, diaryRegisterRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(diaryResponse);
    }

    /**
     * 특정 월의 다이어리 이모지 목록을 조회합니다.
     *
     * @param babyId 조회할 아기의 ID
     * @param month  조회할 연월 (yyyy-MM 형식, 선택 사항)
     * @return 다이어리 이모지 목록 {@link List} of {@link DiaryEmojiResponse}
     */
    @Operation(summary = "월별 다이어리 이모지 조회", description = "특정 아기의 월별 다이어리 이모지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이모지 목록 반환",
                    content = @Content(schema = @Schema(implementation = DiaryEmojiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/emojis/{babyId}")
    public ResponseEntity<List<DiaryEmojiResponse>> getDiaryEmojisByMonth(@Parameter(description = "이모지를 조회할 아기의 ID", required = true) @PathVariable Integer babyId,
                                                                          @Parameter(description = "조회할 연월 (yyyy-MM 형식, 기본값: 현재 월)", required = false, example = "2025-02") @RequestParam(value = "yearMonth", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        List<DiaryEmojiResponse> diaryEmojiResponseList = diaryService.getEmojiByYearMonth(babyId, month);

        return ResponseEntity.status(HttpStatus.OK).body(diaryEmojiResponseList);
    }

    /**
     * 특정 날짜의 다이어리 정보를 조회합니다.
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param babyId            조회할 아기의 ID
     * @param date              조회할 날짜 (선택 사항, 기본값: 현재 날짜)
     * @return 다이어리 정보 {@link DiaryResponse}, 다이어리가 없을 경우 HTTP 204 응답
     */
    @Operation(summary = "특정 날짜의 다이어리 조회", description = "주 양육자가 특정 아기의 특정 날짜 다이어리를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 정보 반환",
                    content = @Content(schema = @Schema(implementation = DiaryResponse.class))),
            @ApiResponse(responseCode = "204", description = "다이어리 없음 (작성된 다이어리가 없음)",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "조회 권한이 없는 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/babies/{babyId}/date")
    public ResponseEntity<DiaryResponse> getDiaryByDate(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                        @Parameter(description = "다이어리를 조회할 아기의 ID", required = true) @PathVariable Integer babyId,
                                                        @Parameter(description = "조회할 날짜 (yyyy-MM-dd 형식, 기본값: 오늘)", required = false, example = "2025-02-19") @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Integer userId = customUserDetails.getUserId();
        DiaryResponse diaryResponse = diaryService.getDiaryByYearMonth(userId, babyId, date);

        if (Objects.isNull(diaryResponse)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(diaryResponse);
    }

    /**
     * 특정 아기의 다이어리 목록을 조회합니다.
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param babyId            조회할 아기의 ID
     * @param month             조회할 연월 (yyyy-MM 형식, 선택 사항)
     * @param search            검색 키워드 (선택 사항)
     * @return 다이어리 목록 {@link List} of {@link DiaryListResponse}
     */
    @Operation(summary = "월별 다이어리 목록 조회", description = "주 양육자가 특정 아기의 특정 월 다이어리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 목록 반환",
                    content = @Content(schema = @Schema(implementation = DiaryListResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "조회 권한이 없는 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/babies/{babyId}")
    public ResponseEntity<List<DiaryListResponse>> getDiaryList(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                @Parameter(description = "다이어리를 조회할 아기의 ID", required = true) @PathVariable Integer babyId,
                                                                @Parameter(description = "조회할 연월 (yyyy-MM 형식, 기본값: 현재 월)", required = false, example = "2025-02") @RequestParam(value = "yearMonth", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                                                                @Parameter(description = "검색어 (다이어리 내용에 포함된 키워드를 검색)", required = false, example = "행복") @RequestParam(value = "search", required = false) String search) {

        Integer userId = customUserDetails.getUserId();
        List<DiaryListResponse> diaryList = diaryService.getDiaryList(userId, babyId, month, search);

        return ResponseEntity.status(HttpStatus.OK).body(diaryList);
    }

    /**
     * 특정 다이어리의 상세 정보를 조회합니다.
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param diaryId           조회할 다이어리 ID
     * @param babyId            다이어리가 속한 아기의 ID
     * @return 다이어리 상세 정보 {@link DiaryGetResponse}
     */
    @Operation(summary = "특정 다이어리 상세 조회", description = "주 양육자가 특정 아기의 특정 다이어리를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 상세 정보 반환",
                    content = @Content(schema = @Schema(implementation = DiaryGetResponse.class))),
            @ApiResponse(responseCode = "403", description = "조회 권한이 없는 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 다이어리 또는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{diaryId}/babies/{babyId}")
    public ResponseEntity<DiaryGetResponse> getDiaryById(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                         @Parameter(description = "조회할 다이어리의 ID", required = true) @PathVariable Integer diaryId,
                                                         @Parameter(description = "해당 다이어리가 속한 아기의 ID", required = true) @PathVariable Integer babyId) {

        int userId = customUserDetails.getUserId();
        DiaryGetResponse diaryById = diaryService.getDiaryById(userId, babyId, diaryId);

        return ResponseEntity.status(HttpStatus.OK).body(diaryById);
    }

    /**
     * 특정 다이어리 정보를 수정합니다.
     *
     * @param customUserDetails  인증된 사용자 정보
     * @param diaryUpdateRequest 다이어리 수정 요청 정보
     * @param babyId             다이어리가 속한 아기의 ID
     * @param diaryId            수정할 다이어리 ID
     * @return 수정된 다이어리 정보 {@link DiaryResponse}
     */
    @Operation(summary = "다이어리 수정", description = "주 양육자가 특정 다이어리를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "다이어리 수정 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "수정 권한이 없는 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 다이어리 또는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{diaryId}/babies/{babyId}")
    public ResponseEntity<DiaryResponse> updateDiary(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @Parameter(description = "수정할 다이어리가 속한 아기의 ID", required = true) @PathVariable Integer babyId,
                                                     @Parameter(description = "수정할 다이어리의 ID", required = true) @PathVariable Integer diaryId,
                                                     @RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                             schema = @Schema(implementation = DiaryUpdateRequest.class)
                                                     )) DiaryUpdateRequest diaryUpdateRequest) {

        int userId = customUserDetails.getUserId();
        DiaryResponse diaryResponse = diaryService.updateDiary(userId, babyId, diaryId, diaryUpdateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(diaryResponse);
    }

    /**
     * 특정 다이어리를 삭제합니다.
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param babyId            다이어리가 속한 아기의 ID
     * @param diaryId           삭제할 다이어리 ID
     * @return HTTP 204 No Content 응답 (응답 본문 없음)
     */
    @Operation(summary = "다이어리 삭제", description = "주 양육자가 특정 다이어리를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "다이어리 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "삭제 권한이 없는 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 다이어리 또는 아기")
    })
    @DeleteMapping("/{diaryId}/babies/{babyId}")
    public ResponseEntity<Void> deleteDiary(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @Parameter(description = "삭제할 다이어리가 속한 아기의 ID", required = true) @PathVariable Integer babyId,
                                            @Parameter(description = "삭제할 다이어리의 ID", required = true) @PathVariable Integer diaryId) {

        int userId = customUserDetails.getUserId();
        diaryService.deleteDiary(userId, babyId, diaryId);

        return ResponseEntity.noContent().build();
    }


}
