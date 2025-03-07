package com.agarang.domain.alarm.controller;

import com.agarang.domain.alarm.dto.request.NotificationJobRegister;
import com.agarang.domain.alarm.service.NotificationJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.agarang.domain.alarm.controller<br>
 * fileName       : NotificationController.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 6.<br>
 * description    :  알림 기능 api controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.06          Fiat_lux            최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationJobService notificationJobService;

    /**
     * 새로운 알림 작업을 등록합니다.
     *
     * <p>이 메서드는 클라이언트로부터 알림 정보를 받아 알림 등록 작업을 수행합니다.</p>
     *
     * @param notificationJobRegister 등록할 알림 작업의 정보를 담은 요청 객체
     * @return HTTP 201 Created 응답 (응답 본문 없음)
     */
    @Operation(summary = "알림 등록", description = "새로운 알림 작업을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "알림이 성공적으로 등록됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<Void> registerNotification(@RequestBody NotificationJobRegister notificationJobRegister) {
        notificationJobService.registerNotification(notificationJobRegister);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
