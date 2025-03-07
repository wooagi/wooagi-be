package com.agarang.domain.growthStandard.dto.mapper;

import com.agarang.domain.growthStandard.dto.response.GrowthStandardResponse;
import com.agarang.domain.growthStandard.entity.GrowthStandard;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * packageName    : com.agarang.domain.growthStandard.dto.mapper<br>
 * fileName       : GrowthStandardMapper.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 24.<br>
 * description    : 성장 발달 mapper 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24           okeio           최초생성<br>
 * <br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GrowthStandardMapper {
    /**
     * GrowthStandard 엔티티를 GrowthStandardResponse DTO로 변환합니다.
     *
     * <p>
     * 이 메서드는 성장 발달 정보를 포함하는 엔티티 객체를 클라이언트 응답용 DTO로 매핑합니다.
     * </p>
     *
     * @param growthStandard 성장 발달 엔티티 객체
     * @return 변환된 {@link GrowthStandardResponse} DTO
     */
    GrowthStandardResponse mapToGrowthStandardResponse(GrowthStandard growthStandard);
}
