package com.agarang.domain.record.repository;

import com.agarang.domain.record.entity.type.Feeding;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.agarang.domain.record.repository<br>
 * fileName       : FeedingRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/29/25<br>
 * description    : 수유 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/29/25          nature1216          최초생성<br>
 */

public interface FeedingRepository extends JpaRepository<Feeding, Integer> {
}
