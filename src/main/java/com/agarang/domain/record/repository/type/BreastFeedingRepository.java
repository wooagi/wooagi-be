package com.agarang.domain.record.repository.type;

import com.agarang.domain.record.entity.type.BreastFeeding;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.agarang.domain.record.repository.type<br>
 * fileName       : BreastFeedingRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 모유수유 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
public interface BreastFeedingRepository extends JpaRepository<BreastFeeding, Integer> {
}
