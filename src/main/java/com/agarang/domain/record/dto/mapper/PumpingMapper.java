package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.PumpingUpdateRequest;
import com.agarang.domain.record.dto.response.PumpingGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.Pumping;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : PumpingMapper.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 유축 Mapper 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PumpingMapper {
    /**
     * 유축 데이터를 업데이트합니다.
     *
     * <p>
     * `PumpingUpdateRequest` 객체의 값을 사용하여 `Pumping` 엔터티를 업데이트합니다.
     * `null` 값이 포함된 속성은 무시되며, 기존 값을 유지합니다.
     * </p>
     *
     * @param request  업데이트할 데이터를 포함하는 요청 객체
     * @param pumping  업데이트할 대상 `Pumping` 엔터티
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePumpingFromUpdateRequest(PumpingUpdateRequest request, @MappingTarget Pumping pumping);

    /**
     * `Record`와 `Pumping` 엔터티를 `PumpingGetResponse` 객체로 매핑합니다.
     *
     * <p>
     * 이 메서드는 기록 정보와 유축 데이터를 받아 `PumpingGetResponse` 객체로 변환합니다.
     * `record.recordId` 값이 `recordId` 필드로 매핑됩니다.
     * </p>
     *
     * @param record  기록 정보를 포함한 `Record` 엔터티
     * @param pumping 유축 데이터를 포함한 `Pumping` 엔터티
     * @return 변환된 `PumpingGetResponse` 객체
     */
    @Mapping(source = "record.recordId", target = "recordId")
    PumpingGetResponse mapToGetResponse(Record record, Pumping pumping);
}
