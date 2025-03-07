package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.FeedingUpdateRequest;
import com.agarang.domain.record.dto.response.BreastFeedingGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.BreastFeeding;
import com.agarang.domain.record.entity.type.Feeding;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : BreastFeedingMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-27<br>
 * description    : 모유수유 Mapper 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-27          nature1216          최초생성<br>
 * <br>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface BreastFeedingMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBreastFeedingFromUpdateRequest(FeedingUpdateRequest request, @MappingTarget BreastFeeding breastFeeding);

    @Mapping(source = "record.recordId", target = "recordId")
    BreastFeedingGetResponse mapToGetResponse(Record record, Feeding feeding, BreastFeeding breastFeeding);
}
