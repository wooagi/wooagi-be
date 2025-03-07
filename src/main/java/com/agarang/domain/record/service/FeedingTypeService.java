package com.agarang.domain.record.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.request.FeedingCreateRequest;
import com.agarang.domain.record.dto.request.FeedingUpdateRequest;
import com.agarang.domain.record.dto.response.FeedingGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.FeedingType;
import com.agarang.domain.record.entity.type.Feeding;

import java.time.LocalDate;

/**
 * packageName    : com.agarang.domain.record.service<br>
 * fileName       : FeedingTypeService.java<br>
 * author         : nature1216 <br>
 * date           : 1/29/25<br>
 * description    : 수유 타입 service 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/29/25          nature1216          최초생성<br>
 */

public interface FeedingTypeService<R extends FeedingGetResponse, Q extends FeedingUpdateRequest> {
    void createFeedingRecord(Feeding feeding, FeedingCreateRequest request);
    R getFeedingRecord(Record record, Feeding feeding);
    void updateFeedingRecord(Integer recordId, Q request);
    void deleteFeedingRecord(Integer recordId);
    FeedingType getFeedingType();
    Integer getDailyTotalAmount(Baby baby, LocalDate date);
}
