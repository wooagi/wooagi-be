package com.agarang.domain.record.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.custody.entity.Custody;
import com.agarang.domain.custody.entity.CustodyType;
import com.agarang.domain.custody.repository.CustodyRepository;
import com.agarang.domain.custody.service.CustodyService;
import com.agarang.domain.record.dto.mapper.RecordMapper;
import com.agarang.domain.record.dto.request.BaseRecordUpdateRequest;
import com.agarang.domain.record.dto.request.BaseRecordCreateRequest;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.repository.RecordRepository;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.service.UserService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.agarang.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * packageName    : com.agarang.domain.record.service<br>
 * fileName       : RecordCommonService.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-26<br>
 * description    : 기록 도메인의 공통 기능을 수행하는 서비스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-26         nature1216          최초생성<br>
 * <br>
 */
@Service
@RequiredArgsConstructor
public class RecordCommonService {
    private final CustodyService custodyService;
    private final RecordRepository recordRepository;
    private final BabyRepository babyRepository;
    private final UserService userService;
    private final CustodyRepository custodyRepository;
    private final RecordMapper recordMapper;
    private final S3Uploader s3Uploader;

    public static final String PART_CREATE_RECORD_DATA = "record";

    /**
     * 특정 사용자의 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 기록을 조회하고, 사용자와 아기의 보호 권한을 확인합니다.
     * </p>
     *
     * @param userId   기록을 조회하는 사용자 ID
     * @param recordId 조회할 기록의 ID
     * @return 조회된 기록 정보 {@link Record}
     * @throws BusinessException 기록 또는 아기를 찾을 수 없는 경우 발생
     */
    public Record getRecord(Integer userId, Integer recordId) {
        User user = userService.findUserById(userId);
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        Baby baby = babyRepository.findById(record.getBaby().getBabyId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        custodyService.checkCustody(user, baby);
        return record;
    }

    /**
     * 새로운 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 새로운 기록을 생성하고 저장합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 기록 생성 요청 객체
     * @return 생성된 기록의 정보를 포함한 {@link Record}
     * @throws BusinessException 아기 정보를 찾을 수 없거나 보호 권한이 없는 경우 발생
     */
    public Record createRecord(Integer userId, Integer babyId, BaseRecordCreateRequest request) {
        User user = userService.findUserById(userId);

        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        custodyService.checkCustody(user, baby);

        Record record = Record.builder()
                .user(user)
                .baby(baby)
                .startedAt(request.getStartedAt())
                .recordType(request.getRecordType())
                .content(request.getContent())
                .build();

        return recordRepository.save(record);
    }

    /**
     * 새로운 기록을 이미지와 함께 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 기록을 생성하고, 이미지를 저장한 후 기록을 저장합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 기록 생성 요청 객체
     * @param image   업로드할 이미지 파일 (선택 사항)
     * @return 생성된 기록의 정보를 포함한 {@link Record}
     * @throws BusinessException 아기 정보를 찾을 수 없거나 보호 권한이 없는 경우 발생
     */
    public Record createRecord(Integer userId, Integer babyId, BaseRecordCreateRequest request, MultipartFile image) {
        User user = userService.findUserById(userId);

        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        custodyService.checkCustody(user, baby);

        Record record = Record.builder()
                .user(user)
                .baby(baby)
                .startedAt(request.getStartedAt())
                .recordType(request.getRecordType())
                .content(request.getContent())
                .build();
        saveImage(image, record);

        return recordRepository.save(record);
    }

    /**
     * 특정 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 기록을 업데이트하며, 필요한 경우 이미지를 함께 수정할 수 있습니다.
     * </p>
     *
     * @param userId   기록을 업데이트하는 사용자 ID
     * @param recordId 수정할 기록의 ID
     * @param request  기록 수정 요청 객체
     * @param image    업데이트할 이미지 파일 (선택 사항)
     * @throws BusinessException 기록을 찾을 수 없거나 수정 권한이 없는 경우 발생
     */
    public void updateRecord(Integer userId, Integer recordId, BaseRecordUpdateRequest request, MultipartFile image) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        checkUpdateDeletePermission(userId, record);
        updateImage(image, request.getExistingImage(), record);
        recordMapper.updateRecordFromBaseUpdateRequest(request, record);
    }

    /**
     * 특정 기록을 삭제합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 기록을 삭제합니다.
     * </p>
     *
     * @param userId   기록을 삭제하는 사용자 ID
     * @param recordId 삭제할 기록의 ID
     * @throws BusinessException 기록을 찾을 수 없거나 삭제 권한이 없는 경우 발생
     */
    public void deleteRecord(Integer userId, Integer recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        checkUpdateDeletePermission(userId, record);

        recordRepository.delete(record);
    }

    /**
     * 기록 수정 또는 삭제 권한을 확인합니다.
     *
     * <p>
     * 기록을 작성한 사용자 또는 보호 권한을 가진 사용자만 기록을 수정하거나 삭제할 수 있습니다.
     * 보호자의 권한이 `SUB`인 경우 삭제 및 수정이 제한됩니다.
     * </p>
     *
     * @param userId 기록을 수정 또는 삭제하려는 사용자 ID
     * @param record 수정 또는 삭제할 기록
     * @throws BusinessException 권한이 없는 경우 발생
     */
    private void checkUpdateDeletePermission(Integer userId, Record record) {
        User user = userService.findUserById(userId);

        Baby baby = babyRepository.findById(record.getBaby().getBabyId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_PERMISSION_DENIED));

        if(record.getUser().getUserId() != user.getUserId() && custody.getCustodyType() == CustodyType.SUB) {
            throw new BusinessException(ErrorCode.DELETE_MODIFY_PERMISSION_DENIED);
        }
    }

    /**
     * 기록에 대한 이미지를 저장합니다.
     *
     * <p>
     * 주어진 이미지 파일을 업로드하고 기록에 저장합니다.
     * </p>
     *
     * @param image  업로드할 이미지 파일 (nullable)
     * @param record 기록 객체
     */
    private void saveImage(MultipartFile image, Record record) {
        if(Objects.isNull(image)) return;

        String newImage = s3Uploader.uploadPermanent(image, PART_CREATE_RECORD_DATA);
        record.setRecordImage(newImage);
    }

    /**
     * 기록의 이미지를 업데이트합니다.
     *
     * <p>
     * 기존 이미지가 있는 경우 유효성을 검사한 후 삭제하고 새로운 이미지를 업로드합니다.
     * </p>
     *
     * @param image         업로드할 새로운 이미지 파일 (nullable)
     * @param existingImage 기존 이미지 파일 경로 (nullable)
     * @param record        기록 객체
     */
    private void updateImage(MultipartFile image, String existingImage, Record record) {
        boolean isNewImageProvided = Objects.nonNull(image);
        boolean isExistingImagePresent = Objects.nonNull(existingImage);

        if(isNewImageProvided) {

            if(isExistingImagePresent) {
                validateExistingImage(existingImage, record.getRecordImage());
                s3Uploader.deleteImage(existingImage);
            } else if(Objects.nonNull(record.getRecordImage())) {
                s3Uploader.deleteImage(record.getRecordImage());
            }
            String newImage = s3Uploader.uploadPermanent(image, PART_CREATE_RECORD_DATA);
            record.setRecordImage(newImage);

        } else {

            if(isExistingImagePresent) {
                validateExistingImage(existingImage, record.getRecordImage());
            } else {
                if(Objects.nonNull(record.getRecordImage())) {
                    s3Uploader.deleteImage(record.getRecordImage());
                    record.setRecordImage(null);
                }
            }

        }
    }

    /**
     * 기존 이미지의 유효성을 검사합니다.
     *
     * <p>
     * 요청된 기존 이미지가 기록에 저장된 이미지와 일치하는지 확인합니다.
     * </p>
     *
     * @param existingImage 요청된 기존 이미지 경로
     * @param dbImage       데이터베이스에 저장된 이미지 경로
     * @throws BusinessException 기존 이미지가 일치하지 않는 경우 발생
     */
    private void validateExistingImage(String existingImage, String dbImage) {
        if(!existingImage.equals(dbImage)) {
            throw new BusinessException(ErrorCode.INVALID_EXISTING_IMAGE);
        }
    }
}
