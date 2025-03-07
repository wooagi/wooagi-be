package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.SleepUpdateRequest;
import com.agarang.domain.record.dto.response.SleepGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.Sleep;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : SleepMapper.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 수면 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SleepMapper {
    /**
     * 수면 데이터를 업데이트합니다.
     *
     * <p>
     * `SleepUpdateRequest` 객체의 값을 사용하여 `Sleep` 엔터티를 업데이트합니다.
     * `null` 값이 포함된 속성은 무시되며, 기존 값을 유지합니다.
     * </p>
     *
     * @param request 업데이트할 데이터를 포함하는 요청 객체
     * @param sleep   업데이트할 대상 `Sleep` 엔터티
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSleepFromUpdateSleepRequest(SleepUpdateRequest request, @MappingTarget Sleep sleep);

    /**
     * `Record`와 `Sleep` 엔터티를 `SleepGetResponse` 객체로 매핑합니다.
     *
     * <p>
     * 이 메서드는 기록 정보와 수면 데이터를 받아 `SleepGetResponse` 객체로 변환합니다.
     * `record.recordId` 값이 `recordId` 필드로 매핑됩니다.
     * </p>
     *
     * @param record 기록 정보를 포함한 `Record` 엔터티
     * @param sleep  수면 데이터를 포함한 `Sleep` 엔터티
     * @return 변환된 `SleepGetResponse` 객체
     */
    @Mapping(source = "record.recordId", target = "recordId")
    SleepGetResponse mapToGetResponse(Record record, Sleep sleep);
}
