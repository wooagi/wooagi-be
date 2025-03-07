package com.agarang.domain.record.service.impl;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.baby.service.BabyService;
import com.agarang.domain.record.dto.AntipyreticCheckMessage;
import com.agarang.domain.record.dto.AntipyreticInfo;
import com.agarang.domain.record.dto.mapper.AntipyreticMapper;
import com.agarang.domain.record.dto.mapper.MedicationMapper;
import com.agarang.domain.record.dto.request.AntipyreticCheckRequest;
import com.agarang.domain.record.dto.request.MedicationCreateRequest;
import com.agarang.domain.record.dto.request.MedicationUpdateRequest;
import com.agarang.domain.record.dto.response.AntipyreticCheckResponse;
import com.agarang.domain.record.dto.response.MedicationGetResponse;
import com.agarang.domain.record.dto.response.RecordCreateResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.AntipyreticType;
import com.agarang.domain.record.entity.enumeration.MedicationType;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.entity.type.Antipyretic;
import com.agarang.domain.record.entity.type.Medication;
import com.agarang.domain.record.repository.MedicationRepository;
import com.agarang.domain.record.repository.type.AntipyreticRepository;
import com.agarang.domain.record.repository.type.GrowthStatusRepository;
import com.agarang.domain.record.service.RecordCommonService;
import com.agarang.domain.record.service.RecordService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : MedicationService.java<br>
 * author         : nature1216 <br>
 * date           : 1/26/25<br>
 * description    : 약 기록 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/26/25          nature1216          최초생성<br>
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MedicationService implements RecordService<MedicationCreateRequest, MedicationGetResponse, MedicationUpdateRequest> {
    private final RecordCommonService recordCommonService;
    private final BabyService babyService;
    private final MedicationMapper medicationMapper;
    private final AntipyreticMapper antipyreticMapper;
    private final MedicationRepository medicationRepository;
    private final AntipyreticRepository antipyreticRepository;
    private final BabyRepository babyRepository;
    private final GrowthStatusRepository growthStatusRepository;

    public static final int ACETAMINOPHEN_DAILY_MAX_DOSE = 5;
    public static final int IBUPROFEN_DAILY_MAX_DOSE = 4;
    public static final int ACETAMINOPHEN_SAFE_MONTH = 4;
    public static final int IBUPROFEN_SAFE_MONTH = 6;

    /**
     * 새로운 약물 복용 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 약물 복용 기록을 생성하고,
     * `Medication` 엔터티로 저장합니다. 해열제(`ANTIPYRETIC`)인 경우
     * 추가적으로 `Antipyretic` 엔터티를 생성합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 약물 복용 기록 생성 요청 객체
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체
     */
    @Override
    public RecordCreateResponse createRecord(Integer userId, Integer babyId, MedicationCreateRequest request) {
        Record record = recordCommonService.createRecord(userId, babyId, request);

        Medication medication = Medication.builder()
                .record(record)
                .medicationType(request.getMedicationType())
                .build();

        medicationRepository.save(medication);

        if(request.getMedicationType() == MedicationType.ANTIPYRETIC) {
            validAntipyretic(request.getSpecificType(), request.getAmount());

            Antipyretic antipyretic = Antipyretic.builder()
                    .medication(medication)
                    .specificType(request.getSpecificType())
                    .amount(request.getAmount())
                    .build();

            antipyreticRepository.save(antipyretic);
        }


        return RecordCreateResponse.builder()
                .recordId(record.getRecordId())
                .build();
    }

    /**
     * 특정 약물 복용 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 약물 복용 기록을 조회하고,
     * 변환된 {@link MedicationGetResponse} 객체를 반환합니다.
     * 해열제(`ANTIPYRETIC`)인 경우 `Antipyretic` 정보도 포함됩니다.
     * </p>
     *
     * @param userid   기록을 조회하는 사용자 ID
     * @param recordId 조회할 기록의 ID
     * @return 조회된 약물 복용 기록 정보를 포함한 {@link MedicationGetResponse} 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public MedicationGetResponse getRecord(Integer userid, Integer recordId) {
        Record record = recordCommonService.getRecord(userid, recordId);

        Medication medication = getMedication(recordId);

        if(medication.getMedicationType() == MedicationType.OTHERS) {
            return medicationMapper.mapToGetResponse(record, medication);
        } else {
            Antipyretic antipyretic = antipyreticRepository.findById(recordId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));
            return medicationMapper.mapToGetResponse(record, medication, antipyretic);
        }

    }

    /**
     * 특정 약물 복용 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 약물 복용 기록을 업데이트합니다.
     * 약물 유형이 변경되면 기존 데이터를 삭제 후 새로운 데이터를 저장합니다.
     * 해열제(`ANTIPYRETIC`)인 경우 추가적으로 `Antipyretic` 데이터를 업데이트합니다.
     * </p>
     *
     * @param userId   기록을 업데이트하는 사용자 ID
     * @param recordId 수정할 기록의 ID
     * @param request  약물 복용 기록 수정 요청 객체
     * @param image    업데이트할 이미지 파일 (선택 사항)
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void updateRecord(Integer userId, Integer recordId, MedicationUpdateRequest request, MultipartFile image) {
        request.validate();
        recordCommonService.updateRecord(userId, recordId, request, image);

        Medication medication = getMedication(recordId);

        MedicationType currentType = medication.getMedicationType();
        MedicationType newType = request.getMedicationType();

        if(!currentType.equals(newType)) {
            deleteMedication(recordId, currentType);
            medication.setMedicationType(newType);

            if(newType == MedicationType.ANTIPYRETIC) {
                Antipyretic antipyretic = Antipyretic.builder()
                        .medication(medication)
                        .specificType(request.getSpecificType())
                        .amount(request.getAmount())
                        .build();

                antipyreticRepository.save(antipyretic);
            }
        } else if(newType == MedicationType.ANTIPYRETIC) {
            Antipyretic antipyretic = antipyreticRepository.findById(recordId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

            antipyreticMapper.updateAntipyreticFromUpdateRequest(request, antipyretic);
        }
    }

    /**
     * 특정 약물 복용 기록을 삭제합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 기록을 삭제합니다.
     * 해열제(`ANTIPYRETIC`)인 경우 `Antipyretic` 데이터도 삭제됩니다.
     * </p>
     *
     * @param userId   기록을 삭제하는 사용자 ID
     * @param recordId 삭제할 기록의 ID
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void deleteRecord(Integer userId, Integer recordId) {
        Medication medication = getMedication(recordId);

        if(medication.getMedicationType() == MedicationType.ANTIPYRETIC) {
            Antipyretic antipyretic = antipyreticRepository.findById(recordId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

            antipyreticRepository.delete(antipyretic);
        }

        medicationRepository.delete(medication);
        recordCommonService.deleteRecord(userId, recordId);
    }

    /**
     * 약물 복용 기록의 타입을 반환합니다.
     *
     * @return 약물 복용 기록 타입 {@link RecordType#MEDICATION}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.MEDICATION;
    }

    /**
     * 해열제 복용 가능 여부를 체크합니다.
     *
     * <p>
     * 주어진 아기 ID와 해열제 복용 요청을 기반으로,
     * 연령 안전성, 2시간 내 복용 여부, 같은 종류의 해열제 4시간 내 복용 여부,
     * 용량 안전성, 하루 최대 복용량 초과 여부 등을 검사합니다.
     * </p>
     *
     * @param babyId  해열제 복용 체크를 수행할 아기의 ID
     * @param request 해열제 복용 체크 요청 객체
     * @return 해열제 복용 가능 여부를 포함한 {@link AntipyreticCheckResponse} 객체
     */
    public AntipyreticCheckResponse checkAntipyretic(Integer babyId, AntipyreticCheckRequest request) {
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        List<AntipyreticInfo> antipyreticInfos = antipyreticRepository.findAntipyreticByBabyAndStartedAt(baby, request.recordId(), start, end);

        AtomicReference<String> isDoesSafeMessage = new AtomicReference<>("");

        boolean isAgeSafe = checkAntipyreticAge(babyId, request.antipyreticType());
        boolean is2hPassed = is2hPassed(antipyreticInfos);
        boolean is4hPassedSinceSameDoes = is4hPassedSinceSameDoes(antipyreticInfos, request.antipyreticType());
        boolean isDoseSafe = isDoesSafe(request.amount(), baby, isDoesSafeMessage);
        boolean isDailySafe = isDailySafe(antipyreticInfos, request.antipyreticType());

        String isAgeSafeMessage = isAgeSafe ? "" : AntipyreticCheckMessage.AGE_IS_NOT_SAFE.getMessage();
        String is2hPassedMessage = is2hPassed ? "" : AntipyreticCheckMessage.HAS_TAKEN_WITHIN_2H.getMessage();
        String is4hPassedSinceSameDoesMessage = is4hPassedSinceSameDoes
                ? ""
                : AntipyreticCheckMessage.HAS_TAKEN_SAME_WITHIN_4H.getMessage();
        String isDailySafeMessage = isDailySafe ? "" : AntipyreticCheckMessage.DAILY_AMOUNT_IS_NOT_SAFE.getMessage();

        return new AntipyreticCheckResponse(
                isAgeSafe,
                is2hPassed,
                is4hPassedSinceSameDoes,
                isDoseSafe,
                isDailySafe,
                isAgeSafeMessage,
                is2hPassedMessage,
                is4hPassedSinceSameDoesMessage,
                isDoesSafeMessage.get(),
                isDailySafeMessage
        );
    }

    /**
     * 특정 기록 ID에 해당하는 약물 복용 기록을 조회합니다.
     *
     * @param recordId 조회할 기록의 ID
     * @return 조회된 {@link Medication} 엔터티
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    private Medication getMedication(Integer recordId) {
        return medicationRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));
    }

    /**
     * 특정 약물 기록을 삭제합니다.
     *
     * <p>
     * 해열제(`ANTIPYRETIC`)인 경우 `Antipyretic` 데이터도 삭제합니다.
     * </p>
     *
     * @param recordId       삭제할 기록의 ID
     * @param medicationType 삭제할 약물 유형
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    private void deleteMedication(Integer recordId, MedicationType medicationType) {
        if(medicationType == MedicationType.ANTIPYRETIC) {
            Antipyretic antipyretic = antipyreticRepository.findById(recordId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));
            antipyreticRepository.delete(antipyretic);
        }
    }

    /**
     * 해열제 복용 요청의 유효성을 검사합니다.
     *
     * <p>
     * 해열제의 유형과 복용량이 `null`이 아니어야 합니다.
     * </p>
     *
     * @param type   해열제 유형
     * @param amount 복용량
     * @throws BusinessException 유효하지 않은 요청일 경우 발생
     */
    private void validAntipyretic(AntipyreticType type, BigDecimal amount) {
        if(type == null || amount == null) {
            throw new BusinessException(ErrorCode.INVALID_CREATE_REQUEST);
        }
    }

    /**
     * 아기의 연령에 따른 해열제 복용 가능 여부를 체크합니다.
     *
     * <p>
     * 아기의 월령을 계산하여 해열제 유형별 안전한 복용 연령 기준을 확인합니다.
     * </p>
     *
     * @param babyId 아기 ID
     * @param type   해열제 유형
     * @return 안전하게 복용 가능한 경우 `true`, 그렇지 않으면 `false`
     */
    private boolean checkAntipyreticAge(Integer babyId, AntipyreticType type) {
        int month = babyService.calculateDaysSinceBirth(babyId) / 30;

        if(type == AntipyreticType.ACETAMINOPHEN && month < ACETAMINOPHEN_SAFE_MONTH) {
            return false;
        } else if (type == AntipyreticType.IBUPROFEN && month < IBUPROFEN_SAFE_MONTH) {
            return false;
        }
        return true;
    }

    /**
     * 최근 2시간 내에 해열제 복용 여부를 체크합니다.
     *
     * <p>
     * 해열제 복용 기록 중 최근 2시간 이내에 복용된 기록이 없는 경우 `true`를 반환합니다.
     * </p>
     *
     * @param list 해열제 복용 기록 리스트
     * @return 최근 2시간 내 복용 이력이 없으면 `true`, 있으면 `false`
     */
    private boolean is2hPassed(List<AntipyreticInfo> list) {
        return list.stream()
                .noneMatch(
                        record -> record.startedAt().isAfter(
                                LocalDateTime.now().minusHours(2)
                        )
                );
    }

    /**
     * 동일한 해열제 복용 후 4시간이 지났는지 확인합니다.
     *
     * <p>
     * 같은 유형의 해열제를 4시간 이내에 복용했는지 여부를 체크합니다.
     * </p>
     *
     * @param list 해열제 복용 기록 리스트
     * @param type 체크할 해열제 유형
     * @return 4시간 이상 경과했으면 `true`, 그렇지 않으면 `false`
     */
    private boolean is4hPassedSinceSameDoes(List<AntipyreticInfo> list, AntipyreticType type) {
        return list.stream()
                .noneMatch(
                        record ->
                                record.antipyreticType().equals(type) &&
                                        record.startedAt().isAfter(LocalDateTime.now().minusHours(4))
                );
    }

    /**
     * 아기의 체중을 기반으로 복용 용량이 안전한지 확인합니다.
     *
     * <p>
     * 아기의 최근 체중 기록을 가져와 권장 복용량과 비교하여 안전 여부를 판단합니다.
     * </p>
     *
     * @param amount            복용량
     * @param baby              아기 정보
     * @param isDoesSafeMessage 복용량이 안전하지 않은 경우 해당 메시지를 설정
     * @return 복용량이 안전하면 `true`, 그렇지 않으면 `false`
     */
    private boolean isDoesSafe(BigDecimal amount, Baby baby, AtomicReference<String> isDoesSafeMessage) {
        BigDecimal weight =  growthStatusRepository.findRecentWeightByBaby(baby)
                .orElseGet(() -> BigDecimal.valueOf(-1));

        if(weight.equals(BigDecimal.valueOf(-1))) {
            isDoesSafeMessage.set(AntipyreticCheckMessage.WEIGHT_RECORD_NOT_FOUND.getMessage());
            return false;
        }

        BigDecimal dosage = weight.multiply(BigDecimal.valueOf(0.4));

        if(!(amount.compareTo(dosage) < 0)) {
            isDoesSafeMessage.set(AntipyreticCheckMessage.DOES_IS_NOT_SAFE.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 하루 최대 해열제 복용 횟수를 초과했는지 확인합니다.
     *
     * <p>
     * 해당 유형의 해열제를 하루 동안 복용한 횟수를 확인하여,
     * 최대 허용 횟수를 초과했는지 판단합니다.
     * </p>
     *
     * @param list 해열제 복용 기록 리스트
     * @param type 체크할 해열제 유형
     * @return 하루 최대 복용 횟수를 초과하지 않았으면 `true`, 그렇지 않으면 `false`
     */
    private boolean isDailySafe(List<AntipyreticInfo> list, AntipyreticType type) {
        long doseCount = list.stream()
                .filter(record -> record.antipyreticType().equals(type))
                .count();

        int maxDose = (type.equals(AntipyreticType.IBUPROFEN))
                ? IBUPROFEN_DAILY_MAX_DOSE
                : ACETAMINOPHEN_DAILY_MAX_DOSE;

        return doseCount <= maxDose;
    }
}
