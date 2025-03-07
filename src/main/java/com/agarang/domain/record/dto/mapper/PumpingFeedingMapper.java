package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.FeedingUpdateRequest;
import com.agarang.domain.record.dto.response.PumpingFeedingGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.Feeding;
import com.agarang.domain.record.entity.type.PumpingFeeding;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : PumpingFeedingMapper.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 유축 수유 Mapper 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PumpingFeedingMapper {
    /**
     * 유축 수유(`PumpingFeeding`) 데이터를 업데이트합니다.
     *
     * <p>
     * `FeedingUpdateRequest` 객체의 값을 사용하여 `PumpingFeeding` 엔터티를 업데이트합니다.
     * `null` 값이 포함된 속성은 무시되며, 기존 값을 유지합니다.
     * </p>
     *
     * @param request        업데이트할 데이터를 포함하는 요청 객체
     * @param pumpingFeeding 업데이트할 대상 `PumpingFeeding` 엔터티
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePumpingFeedingUpdateRequest(FeedingUpdateRequest request, @MappingTarget PumpingFeeding pumpingFeeding);

    /**
     * `Record`, `Feeding`, `PumpingFeeding` 엔터티를 `PumpingFeedingGetResponse` 객체로 매핑합니다.
     *
     * <p>
     * 이 메서드는 수유 기록과 관련된 엔터티 정보를 받아 `PumpingFeedingGetResponse` 객체로 변환합니다.
     * `record.recordId` 값이 `recordId` 필드로 매핑됩니다.
     * </p>
     *
     * @param record         기록 정보를 포함한 `Record` 엔터티
     * @param feeding        수유 관련 데이터를 포함한 `Feeding` 엔터티
     * @param pumpingFeeding 유축 수유 데이터를 포함한 `PumpingFeeding` 엔터티
     * @return 변환된 `PumpingFeedingGetResponse` 객체
     */
    @Mapping(source = "record.recordId", target = "recordId")
    PumpingFeedingGetResponse mapToGetResponse(Record record, Feeding feeding, PumpingFeeding pumpingFeeding);
}
