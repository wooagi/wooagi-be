package com.agarang.domain.alarm.controller;

import com.agarang.domain.alarm.dto.request.FcmTokenRequest;
import com.agarang.domain.alarm.service.FcmService;
import com.agarang.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.agarang.domain.alarm.controller<br>
 * fileName       : FcmTokenController.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 6.<br>
 * description    :  FCM(파이어베이스 클라우드 메시징) 토큰을 관리하는 컨트롤러입니다. 이 컨트롤러는 사용자의 FCM 토큰을 등록하고 삭제하는 기능을 제공합니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.05          Fiat_lux            최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm-tokens")
@Tag(name = "FCM Token API", description = "FCM 푸시 알림을 위한 토큰을 등록 및 관리하는 API")
public class FcmTokenController {
    private final FcmService fcmService;

    /**
     * 사용자의 FCM 토큰을 등록합니다.
     *
     * <p>사용자는 자신의 FCM 토큰을 요청 본문에 포함하여 전송하면,
     * 해당 토큰이 데이터베이스에 저장됩니다.</p>
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param fcmTokenRequest   FCM 토큰을 포함한 요청 객체
     * @return HTTP 200 OK 응답 (응답 본문 없음)
     */
    @Operation(summary = "FCM 토큰 등록", description = "사용자의 FCM 토큰을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "FCM 토큰이 성공적으로 등록됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<Void> registerFcmToken(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                         schema = @Schema(implementation = FcmTokenRequest.class)
                                                 )) FcmTokenRequest fcmTokenRequest) {

        Integer userId = customUserDetails.getUserId();
        fcmService.saveToken(userId, fcmTokenRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 사용자의 FCM 토큰을 삭제합니다.
     *
     * <p>사용자는 자신의 FCM 토큰을 요청 본문에 포함하여 삭제 요청을 보내면,
     * 해당 토큰이 데이터베이스에서 제거됩니다.</p>
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param fcmTokenRequest   삭제할 FCM 토큰을 포함한 요청 객체
     * @return HTTP 204 No Content 응답 (응답 본문 없음)
     */
    @Operation(summary = "FCM 토큰 삭제", description = "사용자의 FCM 토큰을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "FCM 토큰이 성공적으로 삭제됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 FCM 토큰",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteFcmToken(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                               @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                       schema = @Schema(implementation = FcmTokenRequest.class)
                                               )) FcmTokenRequest fcmTokenRequest) {

        Integer userId = customUserDetails.getUserId();
        fcmService.deleteToken(userId, fcmTokenRequest);

        return ResponseEntity.noContent().build();
    }


}
