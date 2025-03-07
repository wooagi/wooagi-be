package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.response.ClipGetResponse;
import com.agarang.domain.record.entity.Record;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : ClipMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-26<br>
 * description    : 클립 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-26          nature1216          최초생성<br>
 * <br>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ClipMapper {
    ClipGetResponse mapToGetResponse(Record record);

    List<ClipGetResponse> mapToGetResponseList(List<Record> records);
}
