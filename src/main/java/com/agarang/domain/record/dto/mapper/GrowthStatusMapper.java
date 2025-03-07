package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.GrowthStatusUpdateRequest;
import com.agarang.domain.record.dto.response.GrowthStatusGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.GrowthStatus;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : GrowthStatusMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-27<br>
 * description    : 발육상태 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-27          nature1216          최초생성<br>
 * <br>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface GrowthStatusMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGrowthStatusFromUpdateRequest(GrowthStatusUpdateRequest request, @MappingTarget GrowthStatus growthStatus);

    @Mapping(source = "record.recordId", target = "recordId")
    GrowthStatusGetResponse mapToGetResponse(Record record, GrowthStatus growthStatus);
}
