package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.response.BathGetResponse;
import com.agarang.domain.record.entity.Record;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : BathMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-26<br>
 * description    : 목욕 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-26          nature1216          최초생성<br>
 * <br>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BathMapper {
    /**
     * `Record` 엔터티를 `BathGetResponse` 객체로 매핑합니다.
     *
     * <p>
     * 이 메서드는 `Record` 엔터티의 데이터를 기반으로 `BathGetResponse` 객체를 생성합니다.
     * 기록된 목욕 정보를 반환하는 데 사용됩니다.
     * </p>
     *
     * @param record 목욕 기록 정보를 포함한 `Record` 엔터티
     * @return 변환된 `BathGetResponse` 객체
     */
    BathGetResponse mapToBathGetResponse(Record record);
}
