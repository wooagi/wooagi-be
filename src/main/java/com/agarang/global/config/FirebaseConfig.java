package com.agarang.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * packageName    : com.agarang.global.config<br>
 * fileName       : ChatRoomService.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-10<br>
 * description    : Firebase 관련 config 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.10          Fiat_lux           최초생성<br>
 */
@Configuration
public class FirebaseConfig {

    /**
     * Firebase를 초기화하는 메서드입니다.
     *
     * <p>이 메서드는 `@PostConstruct` 어노테이션이 적용되어 있어,
     * Spring 컨텍스트가 로드된 후 자동으로 실행됩니다.</p>
     *
     * <h3>초기화 과정</h3>
     * <ol>
     *     <li>`ServiceAccountKey.json` 파일을 클래스패스에서 로드합니다.</li>
     *     <li>Firebase 인증을 위한 `GoogleCredentials` 객체를 생성합니다.</li>
     *     <li>Firebase가 아직 초기화되지 않았다면, `FirebaseApp.initializeApp()`을 호출하여 초기화합니다.</li>
     * </ol>
     *
     * <h3>예외 처리</h3>
     * <ul>
     *     <li>초기화 중 오류가 발생하면 예외 스택 트레이스를 출력합니다.</li>
     *     <li>실제 운영 환경에서는 로깅 프레임워크(SLF4J, Logback)를 활용하여 오류를 기록하는 것이 권장됩니다.</li>
     * </ul>
     */
    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = new ClassPathResource("ServiceAccountKey.json").getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
