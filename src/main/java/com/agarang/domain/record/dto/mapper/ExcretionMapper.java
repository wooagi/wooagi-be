package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.ExcretionUpdateRequest;
import com.agarang.domain.record.dto.response.ExcretionGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.Excretion;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : ExcretionMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-24<br>
 * description    : 배변 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-24          nature1216          최초생성<br>
 * <br>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ExcretionMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateExcretionFromUpdateRequest(ExcretionUpdateRequest request, @MappingTarget Excretion excretion);

    @Mapping(source = "record.recordId", target = "recordId")
    ExcretionGetResponse mapToGetResponse(Record record, Excretion excretion);
}
