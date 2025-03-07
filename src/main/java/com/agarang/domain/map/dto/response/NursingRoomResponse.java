package com.agarang.domain.map.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.map.dto.response<br>
 * fileName       : NursingRoomResponse.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 3.<br>
 * description    : NursingRoom Entity 조회에 대한 response DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.03          okeio           최초생성<br>
 * 25.02.06          okeio           일부 필드 추가<br>
 * 25.02.09          okeio           id 식별자 포함<br>
 * <br>
 */
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "수유실 조회 응답 DTO")
public class NursingRoomResponse {
    @Schema(description = "수유실 ID", example = "1001")
    private Long id;

    @Schema(description = "수유실 이름", example = "강남구 보건소 수유실")
    private String name;

    @Schema(description = "수유실 주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;

    @Schema(description = "수유실 상세 위치", example = "보건소 2층")
    private String location;

    @Schema(description = "위도", example = "37.4979")
    private Double latitude;

    @Schema(description = "경도", example = "127.0276")
    private Double longitude;

    @Schema(description = "전화번호", example = "02-5678-1234")
    private String phone;

    @Schema(description = "수유실 유형명", example = "가족수유실")
    private String roomTypeName;

    @Schema(description = "수유실 유형 코드", example = "3(가족수유실), 4(모유수유/착유실)")
    private Integer roomTypeCode;

    @Schema(description = "아버지 이용 가능 여부", example = "아빠 이용 가능")
    private String fatherUseName;

    @Schema(description = "아버지 이용 가능 코드", example = "0 (불가능), 1 (가능)")
    private Integer fatherUseCode;
}
