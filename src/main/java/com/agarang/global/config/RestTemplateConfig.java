package com.agarang.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * packageName    : com.agarang.global.config<br>
 * fileName       : RestTemplateConfig.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-01-24<br>
 * description    : RestTemplate 설정을 담당하는 구성 클래스입니다.
 * <p>이 클래스는 RESTful API 호출을 수행할 수 있도록 Spring의 {@link RestTemplate} 빈을 생성합니다.</p><br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          Fiat_lux           최초생성<br>
 */
@Configuration
public class RestTemplateConfig {

    /**
     * REST API 호출을 위한 {@link RestTemplate} 빈을 생성합니다.
     *
     * <p>이 메서드를 통해 RestTemplate 객체를 스프링 컨테이너에 등록하여 애플리케이션 내에서
     * HTTP 요청을 수행할 수 있도록 합니다.</p>
     *
     * <h3>사용 예제</h3>
     * <pre>
     * &#64;Autowired
     * private RestTemplate restTemplate;
     *
     * public String getExample() {
     *     String url = "https://api.example.com/data";
     *     return restTemplate.getForObject(url, String.class);
     * }
     * </pre>
     *
     * @return {@link RestTemplate} 객체
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
