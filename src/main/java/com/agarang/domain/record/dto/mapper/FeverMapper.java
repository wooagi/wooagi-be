package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.FeverUpdateRequest;
import com.agarang.domain.record.dto.response.FeverGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.Fever;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : FeverMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-27<br>
 * description    : 열 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-27          nature1216          최초생성<br>
 * <br>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface FeverMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFeverFromUpdateRequest(FeverUpdateRequest request, @MappingTarget Fever fever);

    @Mapping(source = "record.recordId", target = "recordId")
    FeverGetResponse mapToGetResponse(Record record, Fever fever);
}
