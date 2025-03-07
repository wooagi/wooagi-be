package com.agarang.domain.custody.service;

import com.agarang.domain.baby.dto.response.BabyResponse;
import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.custody.dto.request.CustodyInviteCodeRequest;
import com.agarang.domain.custody.dto.request.CustodyInviteRequest;
import com.agarang.domain.custody.dto.response.CustodyInviteResponse;
import com.agarang.domain.custody.dto.response.CustodyResponse;
import com.agarang.domain.custody.entity.Custody;
import com.agarang.domain.custody.entity.CustodyType;
import com.agarang.domain.custody.repository.CustodyRepository;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * packageName    : com.agarang.domain.custody.service<br>
 * fileName       : CustodyService.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    : 보호자(Custody) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * <p>이 클래스는 보호자 목록 조회, 보호자 삭제, 초대 코드 생성 및 검증 등의 기능을 제공합니다.</p><br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          Fiat_lux           최초생성<br>
 * 25.01.30          Fiat_lux           exception custom 처리<br>
 * 25.01.30          nature1216         양육권 존재여부 확인 메소드 추가<br>
 * 25.01.31          Fiat_lux           custody invite code <br>
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustodyService {
    private final CustodyRepository custodyRepository;
    private final UserRepository userRepository;
    private final BabyRepository babyRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String INVITE_PREFIX = "custody:invite:";
    private static final Duration INVITE_EXPIRATION = Duration.ofMinutes(30);

    /**
     * 특정 아기의 보호자 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param babyId 보호자를 조회할 아기의 ID
     * @return 보호자 목록 {@link List} of {@link CustodyResponse}
     * @throws BusinessException 사용자가 존재하지 않거나, 보호자 권한이 없을 경우 예외 발생
     */
    public List<CustodyResponse> getCustodyListByBaby(Integer userId, Integer babyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody userCustody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!CustodyType.MAIN.equals(userCustody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        return custodyRepository.findByBabyAndDeletedAtIsNullAndUserNot(baby, user)
                .stream()
                .sorted((c1, c2) -> {
                    int typeComparison = c1.getCustodyType().compareTo(c2.getCustodyType());
                    return (typeComparison != 0) ? typeComparison : c1.getCreatedAt().compareTo(c2.getCreatedAt());
                })
                .map(cus -> new CustodyResponse(
                        cus.getUser().getUserId(),
                        cus.getUser().getUserImage(),
                        cus.getUser().getName(),
                        cus.getCustodyType().name()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 사용자가 직접 보호자 권한을 삭제합니다.
     *
     * @param userId 사용자 ID
     * @param babyId 보호자 권한을 삭제할 아기의 ID
     */
    @Transactional
    public void deleteBabyManagementByMySelf(Integer userId, Integer babyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        custody.setDeletedAt(LocalDateTime.now());
    }

    /**
     * 사용자가 다른 보호자의 권한을 삭제합니다.
     *
     * @param userId        사용자 ID
     * @param babyId        보호자 권한을 삭제할 아기의 ID
     * @param anotherUserId 삭제할 보호자의 사용자 ID
     */
    @Transactional
    public void deleteBabyManagementByAnother(Integer userId, Integer babyId, Integer anotherUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        User anotherUser = userRepository.findById(anotherUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Custody userCustody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!CustodyType.MAIN.equals(userCustody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        Custody anotherCustody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(anotherUser, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (CustodyType.MAIN.equals(anotherCustody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        anotherCustody.setDeletedAt(LocalDateTime.now());
    }

    public void checkCustody(User user, Baby baby) {
        custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_PERMISSION_DENIED));
    }

    /**
     * 보호자 초대 코드를 생성합니다.
     *
     * @param userId               사용자 ID
     * @param custodyInviteRequest 초대 요청 정보
     * @return 생성된 초대 코드 {@link CustodyInviteResponse}
     */
    @Transactional
    public CustodyInviteResponse generateInviteCode(Integer userId, CustodyInviteRequest custodyInviteRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(custodyInviteRequest.getBabyId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!CustodyType.MAIN.equals(custody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        String inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Map<String, Object> inviteData = new HashMap<>();
        inviteData.put("babyId", custodyInviteRequest.getBabyId());
        inviteData.put("custodyType", custodyInviteRequest.getCustodyType());

        try {
            String inviteJson = objectMapper.writeValueAsString(inviteData);
            redisTemplate.opsForValue().set(INVITE_PREFIX + inviteCode, inviteJson, INVITE_EXPIRATION);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.CUSTODY_SERIALIZATION_ERROR);
        }

        return new CustodyInviteResponse(inviteCode);
    }

    /**
     * 보호자 초대 코드를 검증하고 보호자 권한을 추가합니다.
     *
     * @param userId                   사용자 ID
     * @param custodyInviteCodeRequest 초대 코드 검증 요청 정보
     * @return 초대된 보호자가 관리할 아기의 정보 {@link BabyResponse}
     */
    @Transactional
    public BabyResponse validateInviteCode(Integer userId, CustodyInviteCodeRequest custodyInviteCodeRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String inviteJson = redisTemplate.opsForValue().get(INVITE_PREFIX + custodyInviteCodeRequest.getInviteCode());

        if (Objects.isNull(inviteJson) || inviteJson.isBlank()) {
            throw new BusinessException(ErrorCode.CUSTODY_INVALID_INVITE_CODE);
        }

        Map<String, Object> map;
        try {
            map = objectMapper.readValue(inviteJson, Map.class);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.CUSTODY_SERIALIZATION_ERROR);
        }

        Integer babyId = (Integer) map.get("babyId");
        String custodyTypeStr = (String) map.get("custodyType");

        if (Objects.isNull(babyId) || Objects.isNull(custodyTypeStr)) {
            throw new BusinessException(ErrorCode.CUSTODY_SERIALIZATION_ERROR);
        }

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Optional<Custody> optionalCustody = custodyRepository.findByUserAndBaby(user, baby);

        CustodyType custodyType;
        try {
            custodyType = CustodyType.valueOf(custodyTypeStr);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.CUSTODY_SERIALIZATION_ERROR);
        }

        if (optionalCustody.isPresent()) {
            Custody custody = optionalCustody.get();

            if (Objects.isNull(custody.getDeletedAt())) {
                throw new BusinessException(ErrorCode.CUSTODY_ALREADY_ERROR);
            }
            custody.setDeletedAt(null);
            custody.setCustodyType(custodyType);
            return new BabyResponse(baby.getBabyId());
        }

        Custody custody = new Custody(baby, user, custodyType);
        custodyRepository.save(custody);

        redisTemplate.delete(INVITE_PREFIX + custodyInviteCodeRequest.getInviteCode());

        return new BabyResponse(baby.getBabyId());
    }
}
