package com.agarang.global.s3;

/**
 * packageName    : com.agarang.global.s3<br>
 * fileName       : S3DefaultImage.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 13.<br>
 * description    : S3에 저장된 기본 프로필 이미지 URL을 정의하는 클래스입니다.
 * <p>이 클래스는 사용자 및 아기의 기본 프로필 이미지를 제공하는 상수(static final) 필드로 구성됩니다.
 * AWS S3 버킷에서 기본 이미지를 가져올 때 사용됩니다.</p>
 *
 * <p>이 클래스는 정적 필드만 포함하는 유틸리티 클래스이므로 객체 생성을 방지하기 위해 private 생성자를 사용합니다.</p><br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.13         Fiat_lux            최초생성<br>
 */
public class S3DefaultImage {
    public static final String BABY_DEFAULT_IMAGE = "https://agarang.s3.ap-northeast-2.amazonaws.com/default/baby/baby_profile_01.png";
    public static final String USER_MALE_DEFAULT_IMAGE = "https://agarang.s3.ap-northeast-2.amazonaws.com/default/user_profile/daddy.png";
    public static final String USER_FEMALE_DEFAULT_IMAGE = "https://agarang.s3.ap-northeast-2.amazonaws.com/default/user_profile/mommy.png";


    private S3DefaultImage() {
    }
}
