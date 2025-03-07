package com.agarang.domain.custody.repository;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.custody.entity.Custody;
import com.agarang.domain.user.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.custody.repository<br>
 * fileName       : CustodyRepository.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  Custody entity 의 repository 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22           Fiat_lux          최초생성<br>
 */
public interface CustodyRepository extends CrudRepository<Custody, Integer> {
    List<Custody> findByUserAndDeletedAtIsNullAndBabyNotOrderByCreatedAtAsc(User user, Baby baby);

    List<Custody> findByUserAndDeletedAtIsNullOrderByCreatedAtAsc(User user);

    Optional<Custody> findByUserAndBabyAndDeletedAtIsNull(User user, Baby baby);

    List<Custody> findByBabyAndDeletedAtIsNullAndUserNot(Baby baby, User user);

    int countByBabyAndDeletedAtIsNullAndUserNot(Baby baby, User user);

    List<Custody> findByBabyAndDeletedAtIsNull(Baby baby);

    int countByUserAndDeletedAtIsNull(User user);

    boolean existsByBabyAndUser(Baby baby, User user);

    Optional<Custody> findByUserAndBaby(User user, Baby baby);
}
