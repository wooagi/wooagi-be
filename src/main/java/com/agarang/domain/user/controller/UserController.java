package com.agarang.domain.user.controller;

import com.agarang.domain.user.dto.CustomUserDetails;
import com.agarang.domain.user.dto.request.UserReactivateRequest;
import com.agarang.domain.user.dto.request.UserUpdateRequest;
import com.agarang.domain.user.dto.response.UserResponse;
import com.agarang.domain.user.service.UserService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName    : com.agarang.domain.user.controller<br>
 * fileName       : UserController.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 회원 controller 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Tag(name = "회원", description = "회원 관리 API")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorCode.class)))
        }
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public static final String PART_USER_IMAGE = "image";
    public static final String PART_USER_INFO = "info";

    /**
     * 회원 정보를 조회합니다.
     *
     * <p>
     * 인증된 사용자의 정보를 조회하여 반환합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @return 회원 정보를 포함한 {@link UserResponse} 객체
     */
    @Operation(summary = "회원정보 조회", responses = {@ApiResponse(responseCode = "200")}, description = "회원 정보 조회 성공")
    @GetMapping
    public ResponseEntity<UserResponse> getUserInfo(@Parameter(description = "회원 인증 정보", required = true)
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse response = userService.getUserInfo(userDetails.getUserId());

        return ResponseEntity.ok(response);
    }

    /**
     * 회원 탈퇴를 처리합니다.
     *
     * <p>
     * 인증된 사용자의 계정을 비활성화 처리합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @return HTTP 204 No Content 응답
     */
    @Operation(summary = "회원 탈퇴", responses = {@ApiResponse(responseCode = "204")}, description = "탈퇴 성공")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Parameter(description = "회원 인증 정보", required = true)
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deactivateUser(userDetails.getUserId());

        return ResponseEntity.noContent().build();
    }

    /**
     * 회원 정보를 수정합니다.
     *
     * <p>
     * 사용자의 프로필 정보를 업데이트할 수 있으며, 선택적으로 프로필 이미지를 포함할 수 있습니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param request     회원 정보 수정 요청 데이터
     * @param image       회원 프로필 이미지 (선택적)
     * @return 수정된 회원 정보를 포함한 {@link UserResponse} 객체
     */
    @Operation(summary = "회원 정보 수정", responses = {@ApiResponse(responseCode = "200")}, description = "수정 성공")
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserResponse> updateUser(@Parameter(description = "회원 인증 정보", required = true)
                                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                           @Parameter(description = "회원 수정 정보", schema = @Schema(implementation = UserUpdateRequest.class))
                                           @RequestPart(name = PART_USER_INFO, required = false) @Valid UserUpdateRequest request,
                                           @Parameter(description = "회원 프로필 이미지")
                                           @RequestPart(name = PART_USER_IMAGE, required = false) MultipartFile image) {
        UserResponse response = userService.updateUser(userDetails.getUserId(), request, image);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 탈퇴한 회원 계정을 복구합니다.
     *
     * <p>
     * 인증된 사용자의 계정을 재활성화합니다.
     * </p>
     *
     * @param userDetails 인증된 사용자 정보
     * @param request     회원 계정 복구 요청 데이터
     * @return HTTP 204 No Content 응답
     */
    @PutMapping("/reactivate")
    public ResponseEntity<Void> reactivateUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody @Valid UserReactivateRequest request) {
        userService.reactivateUser(userDetails.getUserId(), request);

        return ResponseEntity.noContent().build();
    }
}
