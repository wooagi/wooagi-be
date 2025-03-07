package com.agarang.domain.record.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.custody.service.CustodyService;
import com.agarang.domain.record.dto.LatestStartedAt;
import com.agarang.domain.record.dto.LatestTimeAgoEntry;
import com.agarang.domain.record.dto.mapper.ClipMapper;
import com.agarang.domain.record.dto.mapper.RecordMapper;
import com.agarang.domain.record.dto.response.*;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.repository.RecordRepository;
import com.agarang.domain.record.service.factory.RecordServiceFactory;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.domain.user.service.UserService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * packageName    : com.agarang.domain.record.service<br>
 * fileName       : RecordQueryService.java<br>
 * author         : nature1216 <br>
 * date           : 1/30/25<br>
 * description    : 기록 조회 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/30/25          nature1216          최초생성<br>
 */

@Service
@RequiredArgsConstructor
public class RecordQueryService {
    private final CustodyService custodyService;
    private final ClipMapper clipMapper;
    private final BabyRepository babyRepository;
    private final UserService userService;
    private final RecordRepository recordRepository;
    private final RecordServiceFactory recordServiceFactory;
    private final List<RecordService<?, ?, ?>> recordServices;

    public static final List<RecordType> EXCLUDED_RECORD_TYPES = List.of(RecordType.CLIP, RecordType.GROWTH_STATUS);

    /**
     * 특정 날짜의 클립 목록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 해당 날짜의 클립 기록을 조회하고,
     * 변환된 {@link ClipListResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userId  클립 목록을 조회하는 사용자 ID
     * @param babyId  클립 목록을 조회할 아기의 ID
     * @param date    조회할 날짜
     * @return 해당 날짜의 클립 목록을 포함한 {@link ClipListResponse} 객체
     * @throws BusinessException 아기를 찾을 수 없거나 보호 권한이 없는 경우 발생
     */
    public ClipListResponse getClipList(Integer userId, Integer babyId, LocalDate date) {
        User user = userService.findUserById(userId);
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        custodyService.checkCustody(user, baby);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Record> clips = recordRepository.findAllByRecordTypeAndBabyAndStartedAtBetweenOrderByStartedAtDesc(RecordType.CLIP, baby, start, end);
        List<ClipGetResponse> clipGetResponses = clipMapper.mapToGetResponseList(clips);

        return ClipListResponse.builder()
                .date(date)
                .clips(clipGetResponses)
                .build();
    }

    /**
     * 특정 날짜의 기록 목록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 해당 날짜의 기록을 조회하고,
     * 변환된 {@link RecordListResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userId  기록 목록을 조회하는 사용자 ID
     * @param babyId  기록 목록을 조회할 아기의 ID
     * @param date    조회할 날짜
     * @return 해당 날짜의 기록 목록을 포함한 {@link RecordListResponse} 객체
     * @throws BusinessException 아기를 찾을 수 없거나 보호 권한이 없는 경우 발생
     */
    public RecordListResponse getRecordListByDate(Integer userId, Integer babyId, LocalDate date) {
        User user = userService.findUserById(userId);
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        custodyService.checkCustody(user, baby);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Record> records = recordRepository
                .findAllByBabyAndStartedAtBetweenAndRecordTypeNotInOrderByStartedAtDesc(baby, start, end, EXCLUDED_RECORD_TYPES);
        List<BaseRecordGetResponse> responses = records.stream()
                .map(record -> recordServiceFactory.getService(record.getRecordType())
                        .getRecord(userId, record.getRecordId()))
                .collect(Collectors.toList());

        return RecordListResponse.builder()
                .date(date)
                .records(responses)
                .build();
    }

    /**
     * 카테고리별 가장 최근 기록의 경과 시간을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로, 최근 한 달간의 각 기록 유형별 가장 마지막 기록의 경과 시간을 조회합니다.
     * </p>
     *
     * @param userId  기록 경과 시간을 조회하는 사용자 ID
     * @param babyId  기록 경과 시간을 조회할 아기의 ID
     * @return 각 기록 유형별 최근 기록 경과 시간을 포함한 {@link LatestTimeAgoResponse} 객체
     * @throws BusinessException 아기를 찾을 수 없거나 보호 권한이 없는 경우 발생
     */
    public LatestTimeAgoResponse getRecordTimeAgo(Integer userId, Integer babyId) {
        User user = userService.findUserById(userId);
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        custodyService.checkCustody(user, baby);

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusMonths(1);

        List<LatestStartedAt> latestStartedAts = recordRepository
                .getLatestStartedAtByBabyAndDateBetweenAndRecordType(baby, start, end);

        System.out.println(start);
        System.out.println(end);
        System.out.println(latestStartedAts);

        Map<RecordType, LocalDateTime> latestTimeMap = latestStartedAts.stream()
                .collect(Collectors.toMap(LatestStartedAt::recordType, LatestStartedAt::startedAt));

        List<LatestTimeAgoEntry> data = Arrays.stream(RecordType.values())
                .map(recordType -> new LatestTimeAgoEntry(
                        recordType,
                        calculateTimeAgo(latestTimeMap.get(recordType))
                ))
                .toList();

        return LatestTimeAgoResponse.builder()
                .data(data)
                .build();
    }

    /**
     * 기록이 마지막으로 생성된 시간으로부터 현재까지 경과한 시간을 계산합니다.
     *
     * <p>
     * 경과 시간이 24시간 이상이면 "X일 전"으로 표시하고,
     * 24시간 미만이면 "X시간 전"으로 표시합니다.
     * </p>
     *
     * @param startedAt 마지막 기록이 생성된 시간
     * @return 경과 시간을 나타내는 문자열 (예: "2일 전" 또는 "5시간 전")
     */
    private String calculateTimeAgo(LocalDateTime startedAt) {
        if(startedAt == null) {
            return "";
        }
        Duration duration = Duration.between(startedAt, LocalDateTime.now());
        Integer hours = BigDecimal.valueOf(duration.toMinutes() / 60.0)
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
        int days = Math.toIntExact(duration.toDays());

        return (days > 0) ? days + "일 전" : hours + "시간 전";
    }
}
