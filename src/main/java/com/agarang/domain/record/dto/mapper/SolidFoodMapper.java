package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.SolidFoodUpdateRequest;
import com.agarang.domain.record.dto.response.SolidFoodGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.SolidFood;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : SolidFoodMapper.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 이유식 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SolidFoodMapper {
    /**
     * 이유식 데이터를 업데이트합니다.
     *
     * <p>
     * `SolidFoodUpdateRequest` 객체의 값을 사용하여 `SolidFood` 엔터티를 업데이트합니다.
     * `null` 값이 포함된 속성은 무시되며, 기존 값을 유지합니다.
     * </p>
     *
     * @param request   업데이트할 데이터를 포함하는 요청 객체
     * @param solidFood 업데이트할 대상 `SolidFood` 엔터티
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSolidFoodFromUpdateRequest(SolidFoodUpdateRequest request, @MappingTarget SolidFood solidFood);

    /**
     * `Record`와 `SolidFood` 엔터티를 `SolidFoodGetResponse` 객체로 매핑합니다.
     *
     * <p>
     * 이 메서드는 기록 정보와 이유식 데이터를 받아 `SolidFoodGetResponse` 객체로 변환합니다.
     * `record.recordId` 값이 `recordId` 필드로 매핑됩니다.
     * </p>
     *
     * @param record   기록 정보를 포함한 `Record` 엔터티
     * @param solidFood 이유식 데이터를 포함한 `SolidFood` 엔터티
     * @return 변환된 `SolidFoodGetResponse` 객체
     */
    @Mapping(source = "record.recordId", target = "recordId")
    SolidFoodGetResponse mapToGetResponse(Record record, SolidFood solidFood);
}
