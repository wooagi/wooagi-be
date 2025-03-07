package com.agarang.domain.baby.controller;

import com.agarang.domain.baby.dto.request.BabyRegisterRequest;
import com.agarang.domain.baby.dto.request.BabyUpdateRequest;
import com.agarang.domain.baby.dto.response.BabyGetResponse;
import com.agarang.domain.baby.dto.response.BabyMainResponse;
import com.agarang.domain.baby.dto.response.BabyMineResponse;
import com.agarang.domain.baby.dto.response.BabyResponse;
import com.agarang.domain.baby.service.BabyService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * packageName    : com.agarang.domain.baby.controller<br>
 * fileName       : BabyController.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  아기 entity 에 관한 controller 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          Fiat_lux           최초생성<br>
 * 25.01.31          Fiat_lux           등록, 수정 api 수정<br>
 * 25.02.04          Fiat_lux           상세 조회 api<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/babies")
@Tag(name = "아기 관리 API", description = "아기 정보를 등록, 조회 및 수정하는 API")
public class BabyController {
    private final BabyService babyService;

    public static final String PART_BABY_IMAGE = "image";
    public static final String PART_BABY_INFO = "info";

    /**
     * 새로운 아기를 등록합니다.
     *
     * <p>이 메서드는 아기의 기본 정보를 포함한 요청 데이터를 받아 새로운 아기를 생성합니다.
     * 선택적으로 아기의 프로필 이미지를 함께 업로드할 수 있습니다.</p>
     *
     * @param customUserDetails   인증된 사용자 정보
     * @param image               업로드할 아기 프로필 이미지 (선택 사항)
     * @param babyRegisterRequest 아기 등록 정보를 포함한 요청 객체
     * @return 등록된 아기의 정보 {@link BabyResponse}
     */
    @Operation(summary = "아기 등록", description = "새로운 아기를 등록합니다. 선택적으로 프로필 이미지를 업로드할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "아기가 성공적으로 등록됨",
                    content = @Content(schema = @Schema(implementation = BabyResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BabyResponse> createBaby(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                   @Parameter(description = "아기 프로필 이미지 (선택 사항)") @RequestPart(value = PART_BABY_IMAGE, required = false) MultipartFile image,
                                                   @RequestPart(value = PART_BABY_INFO) @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                           schema = @Schema(implementation = BabyRegisterRequest.class)
                                                   )) @Valid BabyRegisterRequest babyRegisterRequest) {

        Integer userId = customUserDetails.getUserId();
        BabyResponse babyResponse = babyService.createBaby(userId, babyRegisterRequest, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(babyResponse);
    }


    /**
     * 특정 아기의 상세 정보를 조회합니다.
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param babyId            조회할 아기의 ID
     * @return 아기의 상세 정보 {@link BabyGetResponse}
     */
    @Operation(summary = "아기 상세 조회", description = "특정 아기의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아기 상세 정보 반환",
                    content = @Content(schema = @Schema(implementation = BabyGetResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{babyId}")
    public ResponseEntity<BabyGetResponse> getBabyInfo(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                       @Parameter(description = "조회할 아기의 ID", required = true) @PathVariable Integer babyId) {

        Integer userId = customUserDetails.getUserId();
        BabyGetResponse babyGetResponse = babyService.getBabyInfo(babyId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(babyGetResponse);
    }

    /**
     * 특정 아기의 주요 정보를 조회합니다.
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param babyId            조회할 아기의 ID
     * @return 아기의 주요 정보 {@link BabyMainResponse}
     */
    @Operation(summary = "아기 주요 정보 조회", description = "특정 아기의 주요 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아기 주요 정보 반환",
                    content = @Content(schema = @Schema(implementation = BabyMainResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/main/{babyId}")
    public ResponseEntity<BabyMainResponse> getBabyMain(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                        @Parameter(description = "조회할 아기의 ID", required = true) @PathVariable Integer babyId) {

        Integer userId = customUserDetails.getUserId();
        BabyMainResponse mainBaby = babyService.getMainBaby(babyId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(mainBaby);
    }

    /**
     * 사용자가 관리 중인 아기 목록을 조회합니다.
     *
     * @param customUserDetails 인증된 사용자 정보
     * @return 사용자가 관리 중인 아기 목록 {@link List} of {@link BabyMineResponse}
     */
    @Operation(summary = "내가 관리하는 아기 목록 조회", description = "사용자가 관리 중인 아기 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자가 관리하는 아기 목록 반환",
                    content = @Content(schema = @Schema(implementation = BabyMineResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/mine")
    public ResponseEntity<List<BabyMineResponse>> getManageBabyByMeList(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Integer userId = customUserDetails.getUserId();
        List<BabyMineResponse> babyMineResponses = babyService.mineBaby(userId);
        return ResponseEntity.status(HttpStatus.OK).body(babyMineResponses);
    }

    /**
     * 아기 정보를 수정합니다.
     *
     * <p>이 메서드는 기존 아기의 정보를 업데이트하며, 선택적으로 프로필 이미지를 변경할 수 있습니다.</p>
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param image             변경할 아기 프로필 이미지 (선택 사항)
     * @param babyUpdateRequest 수정할 아기 정보를 포함한 요청 객체
     * @return 수정된 아기의 정보 {@link BabyResponse}
     */
    @Operation(summary = "아기 정보 수정", description = "기존 아기의 정보를 수정하며, 선택적으로 프로필 이미지를 변경할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아기 정보가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = BabyResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "사용자에게 권한이 없음",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BabyResponse> updateBaby(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                   @Parameter(description = "변경할 아기 프로필 이미지 (선택 사항)") @RequestPart(value = "image", required = false) MultipartFile image,
                                                   @RequestPart(value = "info") @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                           schema = @Schema(implementation = BabyUpdateRequest.class)
                                                   )) @Valid BabyUpdateRequest babyUpdateRequest) {

        Integer userId = customUserDetails.getUserId();
        BabyResponse babyResponse = babyService.updateBaby(userId, babyUpdateRequest, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(babyResponse);
    }


}
