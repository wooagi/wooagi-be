package com.agarang.domain.custody.controller;

import com.agarang.domain.baby.dto.response.BabyResponse;
import com.agarang.domain.custody.dto.request.CustodyInviteCodeRequest;
import com.agarang.domain.custody.dto.request.CustodyInviteRequest;
import com.agarang.domain.custody.dto.response.CustodyInviteResponse;
import com.agarang.domain.custody.dto.response.CustodyResponse;
import com.agarang.domain.custody.service.CustodyService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName    : com.agarang.domain.custody.controller<br>
 * fileName       : CustodyController.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  Custody entity 의 controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          Fiat_lux           최초생성<br>
 * 25.01.31          Fiat_lux           custody invite code api<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custodies")
@Tag(name = "공동양육 API", description = "아기의 공동양육자 정보를 조회하는 API")
public class CustodyController {
    private final CustodyService custodyService;

    /**
     * 특정 아기의 보호자 목록을 조회합니다.
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param babyId            보호자 목록을 조회할 아기의 ID
     * @return 보호자 목록 {@link List} of {@link CustodyResponse}
     */
    @Operation(summary = "공동양육자 목록 조회", description = "특정 아기의 공동양육자 목록을 조회합니다. (주 양육자만 조회 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공동양육자 목록 반환",
                    content = @Content(schema = @Schema(implementation = CustodyResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "조회 권한이 없는 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기 또는 공동양육자",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{babyId}")
    public ResponseEntity<List<CustodyResponse>> getCustodyListByBaby(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                      @Parameter(description = "조회할 아기의 ID", required = true) @PathVariable Integer babyId) {

        Integer userId = customUserDetails.getUserId();
        List<CustodyResponse> custodyListByBaby = custodyService.getCustodyListByBaby(userId, babyId);

        return ResponseEntity.status(HttpStatus.OK).body(custodyListByBaby);
    }

    /**
     * 사용자가 직접 보호자 권한을 삭제합니다.
     *
     * <p>사용자가 특정 아기에 대한 보호자 권한을 스스로 삭제할 때 사용됩니다.</p>
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param babyId            보호자 권한을 삭제할 아기의 ID
     * @return HTTP 204 No Content 응답 (응답 본문 없음)
     */
    @Operation(summary = "공동양육자 스스로 공동양육 해제", description = "공동양육자가 자신의 공동양육 권한을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "공동양육 권한 삭제 완료"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기 또는 공동양육자 정보 없음",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/my-self/{babyId}")
    public ResponseEntity<Void> deleteBabyManagementByMySelf(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                             @Parameter(description = "공동양육 해제를 요청할 아기의 ID", required = true) @PathVariable Integer babyId) {

        Integer userId = customUserDetails.getUserId();
        custodyService.deleteBabyManagementByMySelf(userId, babyId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 사용자가 다른 보호자의 권한을 삭제합니다.
     *
     * <p>이 메서드는 특정 보호자가 다른 보호자를 제거할 때 사용됩니다.</p>
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param babyId            보호자 권한을 삭제할 아기의 ID
     * @param anotherUserId     삭제할 보호자의 사용자 ID
     * @return HTTP 204 No Content 응답 (응답 본문 없음)
     */
    @Operation(summary = "공동양육자 삭제 (관리자 권한 필요)", description = "주 양육자가 특정 공동양육자를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "공동양육자 삭제 완료"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "삭제 권한이 없는 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기 또는 공동양육자",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{babyId}/another/{anotherUserId}")
    public ResponseEntity<Void> deleteBabyManagementByAnother(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @Parameter(description = "공동양육자를 삭제할 아기의 ID", required = true) @PathVariable Integer babyId,
                                                              @Parameter(description = "삭제할 공동양육자의 사용자 ID", required = true) @PathVariable Integer anotherUserId) {

        Integer userId = customUserDetails.getUserId();
        custodyService.deleteBabyManagementByAnother(userId, babyId, anotherUserId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 보호자 초대 코드를 생성합니다.
     *
     * <p>해당 아기의 보호자가 새로운 보호자를 초대할 때 사용됩니다.</p>
     *
     * @param customUserDetails    인증된 사용자 정보
     * @param custodyInviteRequest 초대 요청 정보
     * @return 생성된 초대 코드 {@link CustodyInviteResponse}
     */
    @Operation(summary = "공동양육 초대 코드 생성", description = "주 양육자가 특정 아기에 대한 공동양육 초대 코드를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공동양육 초대 코드 생성 완료",
                    content = @Content(schema = @Schema(implementation = CustodyInviteResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "초대 생성 권한이 없는 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/generate")
    public ResponseEntity<CustodyInviteResponse> generateInvite(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                @RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                                        schema = @Schema(implementation = CustodyInviteRequest.class)
                                                                )) CustodyInviteRequest custodyInviteRequest) {

        Integer userId = customUserDetails.getUserId();
        CustodyInviteResponse custodyInviteResponse = custodyService.generateInviteCode(userId, custodyInviteRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(custodyInviteResponse);
    }

    /**
     * 보호자 초대 코드를 검증하고 보호자 권한을 추가합니다.
     *
     * <p>초대 코드를 입력하여 새로운 보호자가 등록될 때 사용됩니다.</p>
     *
     * @param customUserDetails        인증된 사용자 정보
     * @param custodyInviteCodeRequest 초대 코드 검증 요청 정보
     * @return 초대된 보호자가 관리할 아기의 정보 {@link BabyResponse}
     */
    @Operation(summary = "공동양육 초대 코드 검증", description = "사용자가 초대 코드를 입력하여 공동양육자로 등록됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공동양육 등록 완료",
                    content = @Content(schema = @Schema(implementation = BabyResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 초대 코드",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "이미 공동양육자로 등록된 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 초대 코드 또는 아기",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/validate")
    public ResponseEntity<BabyResponse> validateInviteCode(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                           @RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                                   schema = @Schema(implementation = CustodyInviteCodeRequest.class)
                                                           )) CustodyInviteCodeRequest custodyInviteCodeRequest) {

        Integer userId = customUserDetails.getUserId();
        BabyResponse babyResponse = custodyService.validateInviteCode(userId, custodyInviteCodeRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(babyResponse);
    }


}
