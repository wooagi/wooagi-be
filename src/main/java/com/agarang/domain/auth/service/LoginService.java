package com.agarang.domain.auth.service;

import com.agarang.domain.auth.dto.LoginUserInfo;
import com.agarang.domain.auth.dto.TokenPair;
import com.agarang.domain.auth.dto.request.KakaoLoginRequest;
import com.agarang.domain.auth.dto.response.KakaoLoginResponse;
import com.agarang.domain.user.dto.mapper.UserMapper;
import com.agarang.domain.user.dto.response.UserResponse;
import com.agarang.domain.user.entity.Sex;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.domain.user.service.UserService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.agarang.global.s3.S3DefaultImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.agarang.domain.auth.service<br>
 * fileName       : AuthService.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-19<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-19          nature1216          최초생성<br>
 * <br>
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    /**
     * 카카오 로그인 또는 회원가입을 처리합니다.
     *
     * <p>
     * 기존 사용자가 존재하면 로그인 처리하고, 존재하지 않으면 회원가입 후 로그인합니다.
     * </p>
     *
     * @param request 카카오 로그인 요청 데이터
     * @return 로그인된 사용자의 액세스 토큰과 리프레시 토큰을 포함한 {@link KakaoLoginResponse} 객체
     */
    public KakaoLoginResponse kakaoLoginOrRegister(KakaoLoginRequest request) {
        LoginUserInfo user = getUserOrRegister(request);
        TokenPair tokens = tokenService.generateTokens(user.getUserId());

        return new KakaoLoginResponse(tokens.getAccessToken(), tokens.getRefreshToken(), user);
    }

    /**
     * 사용자를 조회하거나 존재하지 않을 경우 회원가입을 진행합니다.
     *
     * <p>
     * 카카오 프로바이더 ID를 통해 기존 사용자를 찾고, 존재하면 정보를 업데이트하여 반환합니다.
     * 존재하지 않으면 새 사용자로 등록합니다.
     * </p>
     *
     * @param kakaoLoginRequest 카카오 로그인 요청 데이터
     * @return 로그인한 사용자 정보
     */
    public LoginUserInfo getUserOrRegister(KakaoLoginRequest kakaoLoginRequest) {
        User user = userRepository.findByProviderId(kakaoLoginRequest.getProviderId())
                .map(existingUser -> updateKakaoUserInfo(existingUser, kakaoLoginRequest))
                .orElseGet(() -> registerUser(kakaoLoginRequest));

        if(user.isAnonymized()) {
            user.anonymize();
        }

        return userMapper.mapToLoginUserInfo(user, user.isDeactivated());
    }

    /**
     * 새 카카오 사용자를 등록합니다.
     *
     * <p>
     * 요청 정보를 바탕으로 새 사용자를 생성하고 기본 프로필 이미지를 설정한 후 저장합니다.
     * </p>
     *
     * @param kakaoLoginRequest 카카오 로그인 요청 데이터
     * @return 등록된 {@link User} 객체
     * @throws BusinessException 회원가입 중 오류 발생 시 예외 발생
     */
    public User registerUser(KakaoLoginRequest kakaoLoginRequest) {
        User user = kakaoLoginRequest.toUserEntity();

        try {
            String defaultImageUrl = (user.getSex() == Sex.MALE) ? S3DefaultImage.USER_MALE_DEFAULT_IMAGE : S3DefaultImage.USER_FEMALE_DEFAULT_IMAGE;
            user.setUserImage(defaultImageUrl);
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error during user registration", e);
            throw new BusinessException(ErrorCode.USER_REGISTRATION_FAILED);
        }
    }

    /**
     * 기존 카카오 사용자 정보를 업데이트합니다.
     *
     * <p>
     * 기존 사용자 이메일을 최신 상태로 업데이트하고 저장합니다.
     * </p>
     *
     * @param existingUser 기존 사용자 객체
     * @param kakaoLoginRequest 카카오 로그인 요청 데이터
     * @return 업데이트된 {@link User} 객체
     * @throws BusinessException 업데이트 중 오류 발생 시 예외 발생
     */
    private User updateKakaoUserInfo(User existingUser, KakaoLoginRequest kakaoLoginRequest) {
        try {
            existingUser.setEmail(kakaoLoginRequest.getEmail());
            return userRepository.save(existingUser);
        } catch (Exception e) {
            log.error("Error while updating kakao user info: {}", kakaoLoginRequest.getEmail(), e);
            throw new BusinessException(ErrorCode.USER_REGISTRATION_FAILED);
        }
    }
}
