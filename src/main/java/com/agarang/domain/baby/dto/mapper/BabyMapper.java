package com.agarang.domain.baby.dto.mapper;

import com.agarang.domain.baby.dto.response.BabyResponse;
import com.agarang.domain.baby.entity.Baby;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * packageName    : com.agarang.domain.baby.dto.mapper<br>
 * fileName       : BabyMapper.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  Baby entity 의 Map struct 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          Fiat_lux           최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BabyMapper {

    /**
     * {@link Baby} 엔티티를 {@link BabyResponse} DTO로 변환합니다.
     *
     * @param baby 변환할 아기 엔티티
     * @return 변환된 아기 응답 DTO
     */
    BabyResponse mapToResponse(Baby baby);
}
