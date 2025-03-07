package com.agarang.domain.map.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.map.dto.response<br>
 * fileName       : MedicalFacilityResponse.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 3.<br>
 * description    : MedicalFacility Entity 조회에 대한 response DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.03          okeio           최초생성<br>
 * 25.02.06          okeio           이름변경 및 DTO 생성<br>
 * 25.02.09          okeio           id 식별자 포함<br>
 * <br>
 */
@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "의료 시설 조회 응답 DTO")
public class MedicalFacilityResponse {
    @Schema(description = "의료 시설 ID", example = "1001")
    private Long id;

    @Schema(description = "의료 시설 이름", example = "서울아동병원")
    private String name;

    @Schema(description = "의료 시설 유형", example = "병원")
    private String typeName;

    @Schema(description = "우편번호", example = "04520")
    private String postalCode;

    @Schema(description = "주소", example = "서울특별시 중구 세종대로 110")
    private String address;

    @Schema(description = "전화번호", example = "02-1234-5678")
    private String phone;

    @Schema(description = "위도", example = "37.5665")
    private Double latitude;

    @Schema(description = "경도", example = "126.9780")
    private Double longitude;

    @Schema(description = "진료 과목명", example = "소아과")
    private String medicalDeptName;
}