package com.agarang.domain.record.controller;

import com.agarang.domain.record.dto.request.AntipyreticCheckRequest;
import com.agarang.domain.record.dto.request.BaseRecordCreateRequest;
import com.agarang.domain.record.dto.request.BaseRecordUpdateRequest;
import com.agarang.domain.record.dto.request.ClipCreateRequest;
import com.agarang.domain.record.dto.response.*;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.service.RecordLookupService;
import com.agarang.domain.record.service.RecordQueryService;
import com.agarang.domain.record.service.RecordService;
import com.agarang.domain.record.service.impl.ClipService;
import com.agarang.domain.record.service.impl.MedicationService;
import com.agarang.domain.user.dto.CustomUserDetails;
import com.agarang.global.exception.ErrorCode;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * packageName    : com.agarang.domain.record.controller<br>
 * fileName       : RecordController.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-22<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-22          nature1216          최초생성<br>
 * <br>
 */
@Tag(name = "기록", description = "기록 관리 API")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorCode.class))),
                @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorCode.class)))
        }
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class RecordController {
    private final RecordLookupService recordLookupService;
    private final RecordQueryService recordQueryService;
    private final ClipService clipService;
    private final MedicationService medicationService;

    public static final String PART_RECORD_IMAGE = "image";
    public static final String PART_RECORD_INFO = "info";

    /**
     * 새로운 기록을 생성합니다.
     *
     * <p>
     * 인증된 사용자가 특정 아기의 기록을 생성할 수 있도록 합니다.
     * 요청 본문에는 기록의 유형과 세부 정보가 포함되며, 생성된 기록의 정보를 응답으로 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param babyId      기록을 생성할 아기의 ID (URL 경로 변수)
     * @param request     기록 생성 요청 객체 (기록 유형 및 추가 정보 포함)
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체 (201 Created 상태 코드)
     */
    @Operation(summary = "기록 추가", responses = {@ApiResponse(responseCode = "201")}, description = "기록 추가 성공")
    @PostMapping("/{babyId}")
    public ResponseEntity<RecordCreateResponse> createRecord(@Parameter(description = "회원 인증 정보", required = true)
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @Parameter(description = "아기 id", required = true)
                                                             @PathVariable(name = "babyId") Integer babyId,
                                                             @Parameter(description = "기록 추가 정보", schema = @Schema(implementation = BaseRecordCreateRequest.class))
                                                             @RequestBody @Valid BaseRecordCreateRequest request) {
        RecordType recordType = request.getRecordType();
        RecordService<BaseRecordCreateRequest, BaseRecordGetResponse, BaseRecordUpdateRequest> service =
                recordLookupService.getRecordService(recordType);

        Integer userId = userDetails.getUserId();
        RecordCreateResponse response = service.createRecord(userId, babyId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 이미지와 함께 클립을 추가합니다.
     *
     * <p>
     * 인증된 사용자가 특정 아기의 클립을 생성할 수 있도록 합니다.
     * 요청 본문에는 클립 정보와 선택적으로 이미지가 포함됩니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param babyId      클립을 추가할 아기의 ID (URL 경로 변수)
     * @param image       클립에 첨부할 선택적 이미지 파일
     * @param request     클립 생성 요청 객체 (필수)
     * @return 생성된 클립 정보를 포함한 {@link RecordCreateResponse} 객체 (201 Created 상태 코드)
     */
    @Operation(summary = "이미지와 함께 클립 추가", responses = {@ApiResponse(responseCode = "201")}, description = "클립 추가 성공")
    @PostMapping(
            path = "/clip/{babyId}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<RecordCreateResponse> createClip(@Parameter(description = "회원 인증 정보", required = true)
                                                               @AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @Parameter(description = "아기 id", required = true)
                                                           @PathVariable(name = "babyId") Integer babyId,
                                                           @Parameter(name = "메모 이미지")
                                                           @RequestPart(name = PART_RECORD_IMAGE, required = false) MultipartFile image,
                                                           @Parameter(name = "클립 정보", required = true)
                                                           @RequestPart(name = PART_RECORD_INFO) @Valid ClipCreateRequest request) {
        RecordCreateResponse response = clipService.createRecord(userDetails.getUserId(), babyId, request, image);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 해열제 복용 가능 여부를 체크합니다.
     *
     * <p>
     * 특정 아기의 해열제 복용 가능 여부를 확인하고 결과를 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param babyId      해열제 복용 가능 여부를 확인할 아기의 ID (URL 경로 변수)
     * @param request     해열제 복용 체크 요청 객체
     * @return 해열제 복용 가능 여부를 포함한 {@link AntipyreticCheckResponse} 객체 (200 OK 상태 코드)
     */
    @Operation(summary = "해열제 복용 가능 여부 체크", responses = {@ApiResponse(responseCode = "200")}, description = "체크 성공")
    @PostMapping("/antipyretic/check/{babyId}")
    public ResponseEntity<AntipyreticCheckResponse> antipyreticCheck(@Parameter(description = "회원 인증 정보", required = true)
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @Parameter(description = "아기 id", required = true)
                                                                     @PathVariable(name = "babyId") Integer babyId,
                                                                     @Parameter(description = "해열제 복용 정보", schema = @Schema(implementation = AntipyreticCheckRequest.class), required = true)
                                                                     @RequestBody @Valid AntipyreticCheckRequest request) {
        AntipyreticCheckResponse response = medicationService.checkAntipyretic(babyId, request);

        return ResponseEntity.ok(response);
    }

    /**
     * 기록을 수정합니다.
     *
     * <p>
     * 특정 기록을 수정하고, 변경된 내용을 반영합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param recordId    수정할 기록의 ID (URL 경로 변수)
     * @param image       기록에 첨부할 선택적 이미지 파일
     * @param request     기록 수정 요청 객체
     * @return HTTP 204 No Content 상태 코드
     */
    @Operation(summary = "기록 수정", responses = {@ApiResponse(responseCode = "204")}, description = "기록 수정 성공")
    @PutMapping(
            path = "/{recordId}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Void> updateRecord(@Parameter(description = "회원 인증 정보", required = true)
                                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Parameter(description = "기록 id", required = true)
                                             @PathVariable Integer recordId,
                                             @Parameter(description = "메모 이미지")
                                             @RequestPart(name = PART_RECORD_IMAGE, required = false) MultipartFile image,
                                             @Parameter(description = "기록 수정 정보", required = true)
                                             @RequestPart(name = PART_RECORD_INFO) @Valid BaseRecordUpdateRequest request) {
        RecordService<BaseRecordCreateRequest, BaseRecordGetResponse, BaseRecordUpdateRequest> service =
                recordLookupService.getRecordService(recordId);

        Integer userId = userDetails.getUserId();
        service.updateRecord(userId, recordId, request, image);

        return ResponseEntity.noContent().build();
    }

    /**
     * 기록을 조회합니다.
     *
     * <p>
     * 특정 기록의 정보를 조회하고 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param recordId    조회할 기록의 ID (URL 경로 변수)
     * @return 조회된 기록 정보를 포함한 {@link BaseRecordGetResponse} 객체 (200 OK 상태 코드)
     */
    @Operation(summary = "기록 조회", responses = {@ApiResponse(responseCode = "200")}, description = "기록 조회 성공")
    @GetMapping("/{recordId}")
    public ResponseEntity<BaseRecordGetResponse> getRecord(@Parameter(description = "회원 인증 정보", required = true)
                                                               @AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @Parameter(description = "기록 id", required = true)
                                                           @PathVariable("recordId") Integer recordId) {
        RecordService<BaseRecordCreateRequest, BaseRecordGetResponse, BaseRecordUpdateRequest> service =
                recordLookupService.getRecordService(recordId);

        BaseRecordGetResponse response = service.getRecord(userDetails.getUserId(), recordId);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 일자별 클립 목록을 조회합니다.
     *
     * <p>
     * 특정 아기의 지정된 날짜에 해당하는 클립 목록을 조회하여 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param babyId      클립을 조회할 아기의 ID (URL 경로 변수)
     * @param date        조회할 기준 일자 (yyyy-MM-dd 형식)
     * @return 조회된 클립 목록을 포함한 {@link ClipListResponse} 객체 (200 OK 상태 코드)
     */
    @Operation(summary = "일자별 클립 목록 조회", responses = {@ApiResponse(responseCode = "200")}, description = "목록 조회 성공")
    @GetMapping("/clips/{babyId}")
    public ResponseEntity<ClipListResponse> getClipListByDate(@Parameter(description = "회원 인증 정보", required = true)
                                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @Parameter(description = "아기 id", required = true)
                                                              @PathVariable(name = "babyId") Integer babyId,
                                                              @Parameter(description = "기준일자", required = true)
                                                              @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        ClipListResponse response = recordQueryService.getClipList(userDetails.getUserId(), babyId, date);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 일자별 기록 목록을 조회합니다.
     *
     * <p>
     * 특정 아기의 지정된 날짜에 해당하는 기록 목록을 조회하여 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param babyId      기록을 조회할 아기의 ID (요청 파라미터)
     * @param date        조회할 기준 일자 (yyyy-MM-dd 형식)
     * @return 조회된 기록 목록을 포함한 {@link RecordListResponse} 객체 (200 OK 상태 코드)
     */
    @Operation(summary = "일자별 기록 목록 조회", responses = {@ApiResponse(responseCode = "200")}, description = "목록 조회 성공")
    @GetMapping("/daily-records")
    public ResponseEntity<RecordListResponse> getAllRecordByDate(@Parameter(description = "회원 인증 정보", required = true)
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @Parameter(description = "아기 id", required = true)
                                                                 @RequestParam(name = "babyId") Integer babyId,
                                                                 @Parameter(description = "기준일자", required = true)
                                                                 @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        RecordListResponse response = recordQueryService.getRecordListByDate(userDetails.getUserId(), babyId, date);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 카테고리별 최근 기록의 경과 시간을 조회합니다.
     *
     * <p>
     * 특정 아기의 가장 최근 기록에 대한 경과 시간을 조회하여 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param babyId      기록을 조회할 아기의 ID (요청 파라미터)
     * @return 최근 기록의 경과 시간을 포함한 {@link LatestTimeAgoResponse} 객체 (200 OK 상태 코드)
     */
    @Operation(summary = "카테고리별 최근 기록 경과시간 조회", responses = {@ApiResponse(responseCode = "200")}, description = "목록 조회 성공")
    @GetMapping("/time-ago")
    public ResponseEntity<LatestTimeAgoResponse> getSummaryByDate(@Parameter(description = "회원 인증 정보", required = true)
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @Parameter(description = "아기 id", required = true)
                                                                  @RequestParam(name = "babyId") Integer babyId) {
        LatestTimeAgoResponse response = recordQueryService.getRecordTimeAgo(userDetails.getUserId(), babyId);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 기록을 삭제합니다.
     *
     * <p>
     * 특정 기록을 삭제하고, 해당 데이터는 더 이상 조회할 수 없습니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param recordId    삭제할 기록의 ID (URL 경로 변수)
     * @return HTTP 204 No Content 상태 코드
     */
    @Operation(summary = "기록 삭제", responses = {@ApiResponse(responseCode = "204")}, description = "기록 삭제 성공")
    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(@Parameter(description = "회원 인증 정보", required = true)
                                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Parameter(description = "기록 id", required = true)
                                             @PathVariable("recordId") Integer recordId) {
        RecordService<BaseRecordCreateRequest, BaseRecordGetResponse, BaseRecordUpdateRequest> service =
                recordLookupService.getRecordService(recordId);

        service.deleteRecord(userDetails.getUserId(), recordId);

        return ResponseEntity.noContent().build();
    }

}
