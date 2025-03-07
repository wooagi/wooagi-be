package com.agarang.domain.map.controller;

import com.agarang.domain.map.dto.request.LocationRequest;
import com.agarang.domain.map.dto.response.MedicalFacilityResponse;
import com.agarang.domain.map.dto.response.NursingRoomResponse;
import com.agarang.domain.map.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName    : com.agarang.domain.map.controller<br>
 * fileName       : MapController.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 3.<br>
 * description    : 위치 기반 정보를 제공하는, MedicalFacility, NursingRoom
 * 엔티티에 관한 Controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.03          okeio           최초생성<br>
 * 25.02.06          okeio           url 변경, 약국 추가<br>
 * 25.02.06          okeio           수유실 추가<br>
 * 25.02.07          okeio           산부인과 추가<br>
 * <br>
 */
@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
@Tag(name = "지도 API", description = "사용자의 위치를 기반으로 병원, 수유실 정보를 제공하는 API")
public class MapController {

    private final MapService mapService;

    /**
     * 반경 내 근처 소아청소년과 병원을 조회합니다.
     *
     * <p>
     * 사용자의 현재 위치와 반경(m)을 입력받아 가까운 소아청소년과 병원을 조회합니다.
     * </p>
     *
     * @param locationRequest 사용자 위치 및 반경 정보
     * @return 조회된 소아청소년과 병원 목록
     */
    @Operation(summary = "소아청소년과 조회", description = "반경 -m 이내 근처 소아청소년과 병원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicalFacilityResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/hospitals/pediatrics")
    public ResponseEntity<?> getNearbyPediatrics(
            @Valid @RequestBody @Parameter(description = "사용자 위치 및 반경 범위") LocationRequest locationRequest) {
        List<MedicalFacilityResponse> response = mapService.getPediatricsData(locationRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * 반경 내 근처 산부인과 병원을 조회합니다.
     *
     * <p>
     * 사용자의 현재 위치와 반경(m)을 입력받아 가까운 산부인과 병원을 조회합니다.
     * </p>
     *
     * @param locationRequest 사용자 위치 및 반경 정보
     * @return 조회된 산부인과 병원 목록
     */
    @Operation(summary = "산부인과 조회", description = "반경 -m 이내 근처 산부인과 병원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicalFacilityResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/hospitals/obstetrics")
    public ResponseEntity<?> getNearbyObstetrics(@Valid @RequestBody @Parameter(description = "사용자 위치 및 반경 범위")
                                                 LocationRequest locationRequest) {
        List<MedicalFacilityResponse> response = mapService.getObstetricsData(locationRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * 반경 내 근처 약국을 조회합니다.
     *
     * <p>
     * 사용자의 현재 위치와 반경(m)을 입력받아 가까운 약국을 조회합니다.
     * </p>
     *
     * @param locationRequest 사용자 위치 및 반경 정보
     * @return 조회된 약국 목록
     */
    @Operation(summary = "약국 조회", description = "반경 -m 이내 근처 약국을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicalFacilityResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/pharmacies")
    public ResponseEntity<?> getNearbyPharmacy(@Valid @RequestBody LocationRequest locationRequest) {
        List<MedicalFacilityResponse> responses = mapService.getPharmaciesData(locationRequest);
        return ResponseEntity.ok(responses);
    }

    /**
     * 반경 내 근처 수유실을 조회합니다.
     *
     * <p>
     * 사용자의 현재 위치와 반경(m)을 입력받아 가까운 수유실을 조회합니다.
     * </p>
     *
     * @param locationRequest 사용자 위치 및 반경 정보
     * @return 조회된 수유실 목록
     */
    @Operation(summary = "수유실 조회", description = "반경 -m 이내 근처 수유실을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NursingRoomResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/nursing-rooms")
    public ResponseEntity<?> getNearbyNursingRooms(@Valid @RequestBody LocationRequest locationRequest) {
        List<NursingRoomResponse> response = mapService.getNursingRoomData(locationRequest);
        return ResponseEntity.ok(response);
    }

}
