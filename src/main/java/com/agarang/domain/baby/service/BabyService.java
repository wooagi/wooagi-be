package com.agarang.domain.baby.service;

import com.agarang.domain.baby.dto.mapper.BabyMapper;
import com.agarang.domain.baby.dto.request.BabyRegisterRequest;
import com.agarang.domain.baby.dto.request.BabyUpdateRequest;
import com.agarang.domain.baby.dto.response.*;
import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.chat_room.service.ChatRoomService;
import com.agarang.domain.custody.entity.Custody;
import com.agarang.domain.custody.entity.CustodyType;
import com.agarang.domain.custody.repository.CustodyRepository;
import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.agarang.global.s3.S3DefaultImage;
import com.agarang.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * packageName    : com.agarang.domain.baby.service<br>
 * fileName       : BabyService.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  Baby entity 의 service 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          Fiat_lux           최초생성<br>
 * 25.01.30          Fiat_lux           exception custom 처리<br>
 * 25.01.31          Fiat_lux           등록, 수정 method 수정 <br>
 * 25.02.04          Fiat_lux           상세 조회 method <br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BabyService {
    private final BabyRepository babyRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final BabyMapper babyMapper;
    private final CustodyRepository custodyRepository;
    private final ChatRoomService chatRoomService;

    public static final String PART_CREATE_BABY_DATA = "baby";

    /**
     * 새로운 아기를 등록합니다.
     *
     * @param userId              사용자의 ID
     * @param babyRegisterRequest 아기 등록 요청 정보
     * @param multipartFile       아기 프로필 이미지 (선택 사항)
     * @return 등록된 아기의 정보 {@link BabyResponse}
     * @throws BusinessException 사용자가 존재하지 않을 경우 예외 발생
     */
    public BabyResponse createBaby(Integer userId, BabyRegisterRequest babyRegisterRequest, MultipartFile multipartFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () ->
                                new BusinessException(ErrorCode.USER_NOT_FOUND)
                );

        String image = getImage(multipartFile);

        Baby baby = new Baby(
                babyRegisterRequest.getName(),
                babyRegisterRequest.getBirth(),
                babyRegisterRequest.getSex(),
                image
        );

        Baby saveBaby = babyRepository.save(baby);
        Custody custody = new Custody(baby, user, CustodyType.MAIN);
        custodyRepository.save(custody);
        chatRoomService.saveChatRoom(baby);

        return babyMapper.mapToResponse(saveBaby);
    }

    /**
     * 아기 정보를 수정합니다.
     *
     * @param userId            사용자 ID
     * @param babyUpdateRequest 아기 정보 수정 요청 정보
     * @param multipartFile     아기 프로필 이미지 (선택 사항)
     * @return 수정된 아기의 정보 {@link BabyResponse}
     * @throws BusinessException 사용자, 아기, 또는 보호 권한이 없을 경우 예외 발생
     */
    public BabyResponse updateBaby(Integer userId, BabyUpdateRequest babyUpdateRequest, MultipartFile multipartFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyUpdateRequest.getBabyId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!CustodyType.MAIN.equals(custody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        updateImage(babyUpdateRequest.getBeforeImage(), multipartFile, baby);
        baby.setName(babyUpdateRequest.getName());
        baby.setBirth(babyUpdateRequest.getBirth());
        baby.setSex(babyUpdateRequest.getSex());

        return babyMapper.mapToResponse(baby);
    }

    /**
     * 특정 아기의 주요 정보를 조회합니다.
     *
     * @param babyId 아기의 ID
     * @param userId 사용자 ID
     * @return 아기의 주요 정보 {@link BabyMainResponse}
     * @throws BusinessException 사용자 또는 아기가 존재하지 않을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public BabyMainResponse getMainBaby(Integer babyId, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        List<Custody> custodyList = custodyRepository.findByUserAndDeletedAtIsNullAndBabyNotOrderByCreatedAtAsc(user, baby);

        List<BabySwitchResponse> babySwitchResponses = new ArrayList<>();
        for (Custody custody : custodyList) {
            Baby anotherBaby = custody.getBaby();
            if (Boolean.TRUE.equals(anotherBaby.getFlagDeleted())) {
                continue;
            }
            long birthDay = ChronoUnit.DAYS.between(anotherBaby.getBirth().toLocalDate(), LocalDate.now());
            babySwitchResponses.add(new BabySwitchResponse(
                    anotherBaby.getName(),
                    anotherBaby.getBabyImage(),
                    anotherBaby.getSex(),
                    birthDay
            ));
        }

        return BabyMainResponse.fromEntity(baby, babySwitchResponses);
    }

    /**
     * 사용자가 관리 중인 아기 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자가 관리하는 아기 목록 {@link List} of {@link BabyMineResponse}
     * @throws BusinessException 사용자가 존재하지 않을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public List<BabyMineResponse> mineBaby(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () ->
                                new BusinessException(ErrorCode.USER_NOT_FOUND)
                );

        List<Custody> custodyList = custodyRepository.findByUserAndDeletedAtIsNullOrderByCreatedAtAsc(user);

        List<BabyMineResponse> babyMineResponseList = new ArrayList<>();

        LocalDate today = LocalDate.now();
        for (Custody custody : custodyList) {
            Baby baby = custody.getBaby();
            long birthDay = ChronoUnit.DAYS.between(baby.getBirth().toLocalDate(), today);
            String custodyType = CustodyType.SUB.name();
            if (CustodyType.MAIN.equals(custody.getCustodyType())) {
                custodyType = CustodyType.MAIN.name();
            }
            babyMineResponseList.add(
                    new BabyMineResponse(
                            baby.getBabyId(),
                            baby.getName(),
                            baby.getBabyImage(),
                            custodyType,
                            birthDay,
                            baby.getSex()
                    )
            );
        }
        return babyMineResponseList;
    }

    /**
     * 아기의 성장 상태를 업데이트합니다.
     *
     * @param babyId           아기의 ID
     * @param growthStatusType 성장 상태 유형 {@link GrowthStatusType}
     * @param size             성장 수치 (예: 몸무게, 키 등)
     */
    public void updateGrowthStatus(Integer babyId, GrowthStatusType growthStatusType, BigDecimal size) {
        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(
                        () ->
                                new BusinessException(ErrorCode.BABY_NOT_FOUND)
                );

        if (growthStatusType.equals(GrowthStatusType.HEAD_SIZE)) {
            baby.setHeadSize(size);
            return;
        } else if (growthStatusType.equals(GrowthStatusType.HEIGHT)) {
            baby.setHeight(size);
            return;
        }
        baby.setWeight(size);
    }

    /**
     * 특정 아기의 출생 이후 경과한 일수를 계산합니다.
     *
     * @param babyId 아기의 ID
     * @return 출생 후 경과한 일수
     */
    public Integer calculateDaysSinceBirth(Integer babyId) {
        Baby baby = getBabyById(babyId);

        LocalDate birth = baby.getBirth().toLocalDate();
        LocalDate today = LocalDate.now();

        return (int) ChronoUnit.DAYS.between(birth, today);
    }

    /**
     * 특정 ID의 아기를 조회합니다.
     *
     * @param babyId 아기의 ID
     * @return 조회된 아기 {@link Baby}
     * @throws BusinessException 아기가 존재하지 않을 경우 예외 발생
     */
    public Baby getBabyById(Integer babyId) {
        return babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));
    }

    /**
     * 특정 아기의 상세 정보를 조회합니다.
     *
     * <p>해당 아기와 보호자 관계가 있는 사용자만 정보를 조회할 수 있습니다.</p>
     *
     * @param babyId 조회할 아기의 ID
     * @param userId 요청한 사용자의 ID
     * @return 아기의 상세 정보 {@link BabyGetResponse}
     * @throws BusinessException 사용자가 존재하지 않거나, 아기가 없거나, 보호자 권한이 없을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public BabyGetResponse getBabyInfo(Integer babyId, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () ->
                                new BusinessException(ErrorCode.USER_NOT_FOUND)
                );
        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(
                        () ->
                                new BusinessException(ErrorCode.BABY_NOT_FOUND)
                );
        custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(
                        () ->
                                new BusinessException(ErrorCode.CUSTODY_NOT_FOUND)
                );
        int custodyCount = custodyRepository.countByBabyAndDeletedAtIsNullAndUserNot(baby, user);

        return new BabyGetResponse(
                baby.getBabyId(),
                baby.getBabyImage(),
                baby.getName(),
                baby.getSex(),
                baby.getBirth(),
                custodyCount,
                baby.getWeight(),
                baby.getHeight(),
                baby.getHeadSize()
        );
    }

    /**
     * 아기의 프로필 이미지를 가져옵니다.
     *
     * <p>이미지가 없을 경우 기본 이미지를 반환합니다.</p>
     *
     * @param multipartFile 업로드할 이미지 파일 (선택 사항)
     * @return 업로드된 이미지 URL 또는 기본 이미지 URL
     */
    private String getImage(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            return S3DefaultImage.BABY_DEFAULT_IMAGE;
        }

        return s3Uploader.uploadPermanent(multipartFile, PART_CREATE_BABY_DATA);
    }

    /**
     * 아기의 프로필 이미지를 업데이트합니다.
     *
     * <p>이전 이미지가 존재하면 삭제한 후 새 이미지를 업로드합니다.
     * 새 이미지가 없고 기존 이미지도 없을 경우 기본 이미지로 설정됩니다.</p>
     *
     * @param beforeImage   기존 아기 프로필 이미지 URL (선택 사항)
     * @param multipartFile 새롭게 업로드할 이미지 파일 (선택 사항)
     * @param baby          이미지가 업데이트될 아기 객체 {@link Baby}
     */
    private void updateImage(String beforeImage, MultipartFile multipartFile, Baby baby) {
        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            if (Objects.isNull(beforeImage)) {
                if (baby.getBabyImage().equals(S3DefaultImage.BABY_DEFAULT_IMAGE)) {
                    return;
                }

                String babyImage = baby.getBabyImage();
                s3Uploader.deleteImage(babyImage);
                baby.setBabyImage(S3DefaultImage.BABY_DEFAULT_IMAGE);
            }
        } else {
            String babyImage = baby.getBabyImage();
            if (!babyImage.equals(S3DefaultImage.BABY_DEFAULT_IMAGE)) {
                s3Uploader.deleteImage(babyImage);
            }
            String uploadImage = s3Uploader.uploadPermanent(multipartFile, PART_CREATE_BABY_DATA);
            baby.setBabyImage(uploadImage);
        }
    }
}
