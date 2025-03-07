package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.FeedingUpdateRequest;
import com.agarang.domain.record.dto.response.FormulaFeedingGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.Feeding;
import com.agarang.domain.record.entity.type.FormulaFeeding;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : FormulaFeedingMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-27<br>
 * description    : 분유수유 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-27          nature1216          최초생성<br>
 * <br>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface FormulaFeedingMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFormulaFeedingFromUpdateRequest(FeedingUpdateRequest request, @MappingTarget FormulaFeeding formulaFeeding);

    @Mapping(source = "record.recordId", target = "recordId")
    FormulaFeedingGetResponse mapToGetResponse(Record record, Feeding feeding, FormulaFeeding formulaFeeding);
}
