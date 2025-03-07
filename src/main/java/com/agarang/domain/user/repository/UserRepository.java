package com.agarang.domain.user.repository;

import com.agarang.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.agarang.domain.user.repository<br>
 * fileName       : UserRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 회원 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByProviderId(String providerId);
}
