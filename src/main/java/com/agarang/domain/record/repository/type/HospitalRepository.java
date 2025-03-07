package com.agarang.domain.record.repository.type;

import com.agarang.domain.record.entity.type.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.agarang.domain.record.repository.type<br>
 * fileName       : HospitalRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 병원 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
}
