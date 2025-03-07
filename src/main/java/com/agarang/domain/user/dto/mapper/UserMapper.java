package com.agarang.domain.user.dto.mapper;

import com.agarang.domain.auth.dto.LoginUserInfo;
import com.agarang.domain.user.dto.request.UserUpdateRequest;
import com.agarang.domain.user.dto.response.UserResponse;
import com.agarang.domain.user.entity.User;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.user.dto.mapper<br>
 * fileName       : UserMapper.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 회원 mapper 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    /**
     * {@link User} 엔티티를 {@link UserResponse} 객체로 변환합니다.
     *
     * <p>
     * 사용자 엔티티 정보를 기반으로 응답 객체를 생성하며, 아기 개수를 포함합니다.
     * </p>
     *
     * @param user      사용자 엔티티
     * @param babyCount 사용자와 연결된 아기 개수
     * @return 변환된 {@link UserResponse} 객체
     */
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.ERROR)
    UserResponse mapToUserResponse(User user, Integer babyCount);

    /**
     * {@link UserUpdateRequest} 데이터를 기반으로 기존 {@link User} 엔티티를 업데이트합니다.
     *
     * <p>
     * null 값이 포함된 필드는 업데이트하지 않으며, 기존 값을 유지합니다.
     * </p>
     *
     * @param request 사용자 정보 수정 요청 데이터
     * @param user    업데이트할 사용자 엔티티
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUpdateUserRequest(UserUpdateRequest request, @MappingTarget User user);

    /**
     * {@link User} 엔티티를 {@link LoginUserInfo} 객체로 변환합니다.
     *
     * <p>
     * 사용자 정보와 함께 계정 활성화 가능 여부를 포함하여 로그인 사용자 정보를 생성합니다.
     * </p>
     *
     * @param user       사용자 엔티티
     * @param canActivate 계정 활성화 가능 여부
     * @return 변환된 {@link LoginUserInfo} 객체
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LoginUserInfo mapToLoginUserInfo(User user, boolean canActivate);
}
