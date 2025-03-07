package com.agarang.domain.user.service;

import com.agarang.domain.custody.repository.CustodyRepository;
import com.agarang.domain.user.dto.CustomUserDetails;
import com.agarang.domain.user.dto.mapper.UserMapper;
import com.agarang.domain.user.dto.request.UserReactivateRequest;
import com.agarang.domain.user.dto.request.UserUpdateRequest;
import com.agarang.domain.user.dto.response.UserResponse;
import com.agarang.domain.user.entity.Sex;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.agarang.global.s3.S3DefaultImage;
import com.agarang.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * packageName    : com.agarang.domain.user.service<br>
 * fileName       : UserService.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 회원 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CustodyRepository custodyRepository;
    private final UserMapper userMapper;
    private final S3Uploader s3Uploader;

    public static final String PART_CREATE_USER_DATA = "user";
    public static final String PERMANENT_DELETE_USER_PREFIX = "deleted: ";

    /**
     * 사용자 정보를 조회합니다.
     *
     * <p>
     * 특정 사용자의 정보를 조회하며, 계정이 삭제된 경우 예외를 발생시킬 수 있습니다.
     * </p>
     *
     * @param userId           조회할 사용자 ID
     * @param includeDeletedAt 삭제된 사용자 포함 여부
     * @return 조회된 사용자 정보를 포함한 {@link UserResponse} 객체
     * @throws BusinessException 계정이 삭제된 경우 예외 발생
     */
    public UserResponse getUserInfo(int userId, boolean includeDeletedAt) {
        User user = findUserById(userId);

        if(!includeDeletedAt && Objects.nonNull(user.getDeletedAt())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_DELETED);
        }

        int babyCount = custodyRepository.countByUserAndDeletedAtIsNull(user);

        return userMapper.mapToUserResponse(user, babyCount);
    }

    /**
     * 사용자 정보를 조회합니다.
     *
     * <p>
     * 계정이 삭제된 사용자의 정보는 포함하지 않습니다.
     * </p>
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자 정보를 포함한 {@link UserResponse} 객체
     */
    public UserResponse getUserInfo(int userId) {
        return getUserInfo(userId, false);
    }

    /**
     * 사용자 계정을 비활성화합니다.
     *
     * <p>
     * 사용자의 계정을 삭제된 상태로 변경합니다.
     * </p>
     *
     * @param userId 비활성화할 사용자 ID
     */
    public void deactivateUser(int userId) {
        User user = findUserById(userId);

        user.setDeletedAt(LocalDateTime.now());
    }

    /**
     * 사용자 정보를 업데이트합니다.
     *
     * <p>
     * 사용자 프로필 정보를 수정하고, 새로운 프로필 이미지를 업로드할 수 있습니다.
     * </p>
     *
     * @param userId  업데이트할 사용자 ID
     * @param request 사용자 정보 수정 요청 데이터
     * @param image   사용자 프로필 이미지 (선택적)
     * @return 업데이트된 사용자 정보를 포함한 {@link UserResponse} 객체
     */
    public UserResponse updateUser(Integer userId, UserUpdateRequest request, MultipartFile image) {
        User user = findUserById(userId);

        updateUserImage(image, request.getExistingImage(), user);
        userMapper.updateUserFromUpdateUserRequest(request, user);

        int babyCount = custodyRepository.countByUserAndDeletedAtIsNull(user);

        return userMapper.mapToUserResponse(user, babyCount);
    }

    /**
     * 탈퇴한 사용자 계정을 복구합니다.
     *
     * <p>
     * 사용자의 계정을 다시 활성화하며, 사용자의 요청에 따라 완전한 익명화 처리도 가능합니다.
     * </p>
     *
     * @param userId  복구할 사용자 ID
     * @param request 계정 복구 요청 데이터
     */
    public void reactivateUser(Integer userId, UserReactivateRequest request) {
        User user = findUserById(userId);

        if(!request.reactivate() || user.isAnonymized()) {
            user.anonymize();
            return;
        }

        user.setDeletedAt(null);
    }

    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     *
     * <p>
     * 사용자가 존재하지 않는 경우 예외를 발생시킵니다.
     * </p>
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자 엔티티
     * @throws BusinessException 사용자가 존재하지 않는 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Integer.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        return createUserDetails(user);
    }

    private UserDetails createUserDetails(User user) {
        return new CustomUserDetails(user.getUserId());
    }

    /**
     * 사용자 프로필 이미지를 업데이트합니다.
     *
     * <p>
     * 새로운 이미지가 제공되면 기존 이미지를 삭제하고 업로드하며, 기존 이미지 검증을 수행할 수 있습니다.
     * </p>
     *
     * @param image         새로 업로드할 이미지 파일 (선택적)
     * @param existingImage 기존 이미지 파일 경로 (선택적)
     * @param user          업데이트할 사용자 엔티티
     */
    private void updateUserImage(MultipartFile image, String existingImage, User user) {
        boolean isNewImageProvided = Objects.nonNull(image);
        String dbImage = user.getUserImage();
        String defaultImage = user.getSex().equals(Sex.MALE)
                        ? S3DefaultImage.USER_MALE_DEFAULT_IMAGE
                        : S3DefaultImage.USER_FEMALE_DEFAULT_IMAGE;

        if(isNewImageProvided) {
            if (!dbImage.equals(defaultImage)) {
                if(Objects.nonNull(existingImage)) {
                    validateExistingImage(existingImage, dbImage);
                }
                s3Uploader.deleteImage(dbImage);
            }
            String newImage = s3Uploader.uploadPermanent(image, PART_CREATE_USER_DATA);
            user.setUserImage(newImage);
        } else {
            if(Objects.nonNull(existingImage)) {
                validateExistingImage(existingImage, dbImage);
            } else {
                if(!dbImage.equals(defaultImage)) {
                    s3Uploader.deleteImage(dbImage);
                }
                user.setUserImage(defaultImage);
            }
        }
    }

    /**
     * 제공된 기존 이미지가 데이터베이스에 저장된 이미지와 일치하는지 검증합니다.
     *
     * <p>
     * 일치하지 않을 경우 예외를 발생시킵니다.
     * </p>
     *
     * @param existingImage 사용자가 제공한 기존 이미지 경로
     * @param dbImage       데이터베이스에 저장된 이미지 경로
     * @throws BusinessException 이미지가 일치하지 않는 경우 예외 발생
     */
    private void validateExistingImage(String existingImage, String dbImage) {
        if(!existingImage.equals(dbImage)) {
            throw new BusinessException(ErrorCode.INVALID_EXISTING_IMAGE);
        }
    }
}
