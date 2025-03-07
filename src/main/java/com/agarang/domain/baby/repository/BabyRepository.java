package com.agarang.domain.baby.repository;

import com.agarang.domain.baby.entity.Baby;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.baby.repository<br>
 * fileName       : BabyRepository.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-22<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-22          nature1216          최초생성<br>
 * <br>
 */
@Repository
public interface BabyRepository extends JpaRepository<Baby, Integer> {
    Optional<Baby> findByBabyIdAndFlagDeletedIsFalse(Integer babyId);
}

