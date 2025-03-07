package com.agarang.domain.map.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * packageName    : com.agarang.domain.map.dto.request<br>
 * fileName       : LocationRequest.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 3.<br>
 * description    : 위치기반 지도 API 요청 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.03         okeio           최초생성<br>
 * <br>
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "위치기반 지도 API 요청 DTO")
public class LocationRequest {

    @Schema(description = "위도 (latitude)", example = "37.5665")
    @NotNull
    @Min(-90)
    @Max(90)
    private Double latitude;

    @Schema(description = "경도 (longitude)", example = "126.9780")
    @NotNull
    @Min(-180)
    @Max(180)
    private Double longitude;

    @Schema(description = "검색 반경 (미터)", example = "1000")
    @NotNull
    @Min(1) // 반경 1m
    @Max(50000) // 반경 50000m(50km)
    private Integer radius;

}

