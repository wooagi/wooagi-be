package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.FeedingUpdateRequest;
import com.agarang.domain.record.dto.response.NormalFeedingGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.Feeding;
import com.agarang.domain.record.entity.type.NormalFeeding;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : NormalFeedingMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-26<br>
 * description    : 일반수유 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-26          nature1216          최초생성<br>
 * <br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface NormalFeedingMapper {
    /**
     * 기존의 일반 수유 데이터를 업데이트합니다.
     *
     * <p>
     * 이 메서드는 `FeedingUpdateRequest` 객체의 값을 사용하여 `NormalFeeding` 엔터티를 업데이트합니다.
     * `null` 값이 포함된 속성은 무시되며 기존 값을 유지합니다.
     * </p>
     *
     * @param request        업데이트할 데이터를 포함하는 요청 객체
     * @param normalFeeding  업데이트할 대상 `NormalFeeding` 엔터티
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNormalFeedingFromNormalFeedingUpdateRequest(FeedingUpdateRequest request, @MappingTarget NormalFeeding normalFeeding);

    /**
     * `Record`, `Feeding`, `NormalFeeding` 엔터티를 `NormalFeedingGetResponse` 객체로 매핑합니다.
     *
     * <p>
     * 이 메서드는 수유 기록과 관련된 엔터티 정보를 받아 `NormalFeedingGetResponse` 객체로 변환합니다.
     * `record.recordId` 값이 `recordId` 필드로 매핑됩니다.
     * </p>
     *
     * @param record        기록 정보를 포함한 `Record` 엔터티
     * @param feeding       수유 관련 데이터를 포함한 `Feeding` 엔터티
     * @param normalFeeding 일반 수유 데이터를 포함한 `NormalFeeding` 엔터티
     * @return 변환된 `NormalFeedingGetResponse` 객체
     */
    @Mapping(source = "record.recordId", target = "recordId")
    NormalFeedingGetResponse mapToGetResponse(Record record, Feeding feeding, NormalFeeding normalFeeding);

}
