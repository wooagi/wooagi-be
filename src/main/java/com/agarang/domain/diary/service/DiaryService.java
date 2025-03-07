package com.agarang.domain.diary.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.custody.entity.Custody;
import com.agarang.domain.custody.entity.CustodyType;
import com.agarang.domain.custody.repository.CustodyRepository;
import com.agarang.domain.diary.dto.mapper.DiaryKeywordMapper;
import com.agarang.domain.diary.dto.mapper.DiaryMapper;
import com.agarang.domain.diary.dto.request.DiaryKeywordRequest;
import com.agarang.domain.diary.dto.request.DiaryRegisterRequest;
import com.agarang.domain.diary.dto.request.DiaryUpdateRequest;
import com.agarang.domain.diary.dto.response.*;
import com.agarang.domain.diary.entity.Diary;
import com.agarang.domain.diary.entity.DiaryKeyword;
import com.agarang.domain.diary.repository.DiaryKeywordRepository;
import com.agarang.domain.diary.repository.DiaryRepository;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * packageName    : com.agarang.domain.diary.service<br>
 * fileName       : DiaryService.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 24.<br>
 * description    :  Diary entity 의 service 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          Fiat_lux           최초생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final DiaryMapper diaryMapper;
    private final BabyRepository babyRepository;
    private final UserRepository userRepository;
    private final CustodyRepository custodyRepository;
    private final DiaryKeywordRepository diaryKeywordRepository;
    private final DiaryKeywordMapper diaryKeywordMapper;
    private final KeywordService keywordService;

    /**
     * 특정 월의 다이어리 이모지 목록을 조회합니다.
     *
     * @param babyId    조회할 아기의 ID
     * @param yearMonth 조회할 연월 (선택 사항, 기본값: 현재 연월)
     * @return 다이어리 이모지 목록 {@link List} of {@link DiaryEmojiResponse}
     * @throws BusinessException 미래 날짜를 요청할 경우 예외 발생
     */
    public List<DiaryEmojiResponse> getEmojiByYearMonth(Integer babyId, YearMonth yearMonth) {
        if (Objects.isNull(yearMonth)) {
            yearMonth = YearMonth.now();
        } else if (yearMonth.isAfter(YearMonth.now())) {
            throw new BusinessException(ErrorCode.INVALID_DATE_REQUEST);
        }

        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Diary> diaryList = diaryRepository.findByBabyAndWrittenDateBetween(baby, startDate, endDate);

        return diaryMapper.mapToDiaryEmojiResponseList(diaryList);
    }

    /**
     * 특정 날짜의 다이어리 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @param babyId 다이어리를 조회할 아기의 ID
     * @param date   조회할 날짜 (선택 사항, 기본값: 현재 날짜)
     * @return 다이어리 정보 {@link DiaryResponse}, 다이어리가 없을 경우 null 반환
     * @throws BusinessException 미래 날짜를 요청할 경우 예외 발생
     */
    public DiaryResponse getDiaryByYearMonth(Integer userId, Integer babyId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!Objects.equals(CustodyType.MAIN, custody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        if (Objects.isNull(date)) {
            date = LocalDate.now();
        } else if (date.isAfter(LocalDate.now())) {
            throw new BusinessException(ErrorCode.INVALID_DATE_REQUEST);
        }

        Optional<Diary> optionalDiary = diaryRepository.findByBabyAndWrittenDate(baby, date);

        return optionalDiary.map(diaryMapper::mapToDiaryResponse).orElse(null);
    }

    /**
     * 특정 아기의 다이어리 목록을 조회합니다.
     *
     * @param userId    사용자 ID
     * @param babyId    조회할 아기의 ID
     * @param yearMonth 조회할 연월 (선택 사항, 기본값: 현재 연월)
     * @param search    검색 키워드 (선택 사항)
     * @return 다이어리 목록 {@link List} of {@link DiaryListResponse}
     */
    public List<DiaryListResponse> getDiaryList(Integer userId, Integer babyId, YearMonth yearMonth, String search) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!Objects.equals(CustodyType.MAIN, custody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        if (Objects.isNull(yearMonth)) {
            yearMonth = YearMonth.now();
        }
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Diary> diaryList = diaryRepository.findDiariesByMonthAndSearch(babyId, startDate, endDate, search);
        List<DiaryListResponse> diaryListResponseList = new ArrayList<>();

        for (Diary diary : diaryList) {
            List<DiaryKeyword> diaryKeywords = diaryKeywordRepository.findByDiary(diary);
            List<DiaryKeyWordResponse> diaryKeyWordResponses = diaryKeywordMapper.mapToResponseList(diaryKeywords);

            diaryListResponseList.add(new DiaryListResponse(diary.getDiaryId(), diary.getEmoji(), diary.getWrittenDate(), diaryKeyWordResponses));
        }
        return diaryListResponseList;
    }

    /**
     * 특정 다이어리의 상세 정보를 조회합니다.
     *
     * @param userId  사용자 ID
     * @param babyId  다이어리가 속한 아기의 ID
     * @param diaryId 조회할 다이어리 ID
     * @return 다이어리 상세 정보 {@link DiaryGetResponse}
     */
    public DiaryGetResponse getDiaryById(Integer userId, Integer babyId, Integer diaryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!Objects.equals(CustodyType.MAIN, custody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        Long birthDay = ChronoUnit.DAYS.between(baby.getBirth().toLocalDate(), LocalDate.now());

        List<DiaryKeyword> diaryKeywordList = diaryKeywordRepository.findByDiary(diary);
        List<DiaryKeyWordResponse> diaryKeyWordResponseList = new ArrayList<>();

        diaryKeywordList.forEach(o ->
                diaryKeyWordResponseList.add(new DiaryKeyWordResponse(o.getKeywordId(), o.getName()))
        );

        return new DiaryGetResponse(
                diary.getDiaryId(),
                diary.getContent(),
                diary.getEmoji(),
                diary.getWrittenDate(),
                birthDay,
                diaryKeyWordResponseList
        );
    }

    /**
     * 기존 다이어리의 내용을 수정합니다.
     *
     * <p>이 메서드는 사용자가 특정 다이어리의 내용을 수정하고, 관련 키워드를 업데이트합니다.</p>
     *
     * @param userId             사용자 ID
     * @param babyId             다이어리가 속한 아기의 ID
     * @param diaryId            수정할 다이어리 ID
     * @param diaryUpdateRequest 다이어리 수정 요청 정보
     * @return 수정된 다이어리 정보 {@link DiaryResponse}
     * @throws BusinessException - 사용자가 존재하지 않을 경우 {@link ErrorCode#USER_NOT_FOUND}
     *                           - 아기가 존재하지 않을 경우 {@link ErrorCode#BABY_NOT_FOUND}
     *                           - 보호자 권한이 없을 경우 {@link ErrorCode#CUSTODY_NOT_AUTHORIZED}
     *                           - 다이어리가 존재하지 않을 경우 {@link ErrorCode#DIARY_NOT_FOUND}
     *                           - 삭제할 키워드가 존재하지 않을 경우 {@link ErrorCode#DIARY_KEYWORD_NOT_FOUND}
     *                           - 업데이트할 키워드가 존재하지 않을 경우 {@link ErrorCode#DIARY_KEYWORD_UPDATE_NOT_FOUND}
     *                           - 새로운 키워드를 추가할 때 최대 개수를 초과한 경우 {@link ErrorCode#DIARY_KEYWORD_LIMIT_EXCEEDED}
     */
    @Transactional
    public DiaryResponse updateDiary(Integer userId, Integer babyId, Integer diaryId, DiaryUpdateRequest diaryUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!Objects.equals(CustodyType.MAIN, custody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        diary.setContent(diaryUpdateRequest.getContent());
        diary.setEmoji(diaryUpdateRequest.getEmoji());
        diary.setUser(user);

        List<DiaryKeyword> existingKeywords = diaryKeywordRepository.findByDiary(diary);
        Set<Integer> existingKeywordIds = existingKeywords.stream()
                .map(DiaryKeyword::getKeywordId)
                .collect(Collectors.toSet());

        Set<String> existingKeywordNames = existingKeywords.stream()
                .map(DiaryKeyword::getName)
                .collect(Collectors.toSet());

        List<DiaryKeyword> keywordsAfterDeletion = new ArrayList<>(existingKeywords);

        for (Integer deleteId : diaryUpdateRequest.getDeleteKeyword()) {
            if (!existingKeywordIds.contains(deleteId)) {
                throw new BusinessException(ErrorCode.DIARY_KEYWORD_NOT_FOUND);
            }
            diaryKeywordRepository.deleteById(deleteId);
            keywordsAfterDeletion.removeIf(keyword -> keyword.getKeywordId().equals(deleteId));
        }

        for (DiaryKeywordRequest updateKeyword : diaryUpdateRequest.getUpdateKeyword()) {
            if (!existingKeywordIds.contains(updateKeyword.getId())) {
                throw new BusinessException(ErrorCode.DIARY_KEYWORD_UPDATE_NOT_FOUND);
            }

            if (existingKeywordNames.contains(updateKeyword.getName())) {
                throw new BusinessException(ErrorCode.DIARY_KEYWORD_ALREADY_EXISTS);
            }
            DiaryKeyword diaryKeyword = diaryKeywordRepository.findById(updateKeyword.getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_KEYWORD_NOT_FOUND));
            diaryKeyword.setName(updateKeyword.getName());
        }

        if (keywordsAfterDeletion.size() + diaryUpdateRequest.getNewKeyword().size() > 6) {
            throw new BusinessException(ErrorCode.DIARY_KEYWORD_LIMIT_EXCEEDED);
        }

        List<DiaryKeyword> newKeywords = diaryUpdateRequest.getNewKeyword().stream()
                .map(request -> new DiaryKeyword(request.getName(), diary))
                .toList();

        diaryKeywordRepository.saveAll(newKeywords);

        return diaryMapper.mapToDiaryResponse(diary);
    }

    /**
     * 새로운 다이어리를 등록합니다.
     *
     * <p>이 메서드는 사용자가 특정 날짜의 다이어리를 생성하고, 키워드를 자동으로 추출하여 저장합니다.</p>
     *
     * @param userId               사용자 ID
     * @param babyId               다이어리를 등록할 아기의 ID
     * @param diaryRegisterRequest 다이어리 등록 요청 정보
     * @return 등록된 다이어리 정보 {@link DiaryResponse}
     * @throws BusinessException - 사용자가 존재하지 않을 경우 {@link ErrorCode#USER_NOT_FOUND}
     *                           - 아기가 존재하지 않을 경우 {@link ErrorCode#BABY_NOT_FOUND}
     *                           - 보호자 권한이 없을 경우 {@link ErrorCode#CUSTODY_NOT_AUTHORIZED}
     *                           - 동일한 날짜의 다이어리가 이미 존재할 경우 {@link ErrorCode#DIARY_ALREADY_EXISTS}
     */
    @Transactional
    public DiaryResponse registerDiary(Integer userId, Integer babyId, DiaryRegisterRequest diaryRegisterRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!Objects.equals(CustodyType.MAIN, custody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        if (diaryRepository.existsByBabyAndWrittenDate(baby, diaryRegisterRequest.getWrittenDate())) {
            throw new BusinessException(ErrorCode.DIARY_ALREADY_EXISTS);
        }

        Diary diary = new Diary(
                diaryRegisterRequest.getContent(),
                diaryRegisterRequest.getEmoji(),
                diaryRegisterRequest.getWrittenDate(),
                baby,
                user
        );

        Diary savedDiary = diaryRepository.save(diary);

        List<String> keywords = keywordService.extractKeywords(savedDiary.getContent());
        List<DiaryKeyword> diaryKeywords = keywords.stream()
                .map(keyword -> new DiaryKeyword(keyword, diary))
                .toList();

        diaryKeywordRepository.saveAll(diaryKeywords);

        return diaryMapper.mapToDiaryResponse(savedDiary);
    }

    /**
     * 특정 다이어리를 삭제합니다.
     *
     * @param userId  사용자 ID
     * @param babyId  다이어리가 속한 아기의 ID
     * @param diaryId 삭제할 다이어리 ID
     */
    @Transactional
    public void deleteDiary(Integer userId, Integer babyId, Integer diaryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findByBabyIdAndFlagDeletedIsFalse(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        if (!CustodyType.MAIN.equals(custody.getCustodyType())) {
            throw new BusinessException(ErrorCode.CUSTODY_NOT_AUTHORIZED);
        }

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        diaryKeywordRepository.deleteByDiary(diary);
        diaryRepository.delete(diary);
    }


}
