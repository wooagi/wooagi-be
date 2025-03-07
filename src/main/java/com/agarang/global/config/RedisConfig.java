package com.agarang.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * packageName    : com.agarang.global.config<br>
 * fileName       : RedisConfig.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-01-24<br>
 * description    : Redis 관련 config 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          Fiat_lux           최초생성<br>
 */
@Configuration
public class RedisConfig {

    /**
     * 기본 RedisTemplate을 설정합니다.
     *
     * <p>이 RedisTemplate은 문자열 기반의 키-값 저장소로 사용됩니다.
     * 키와 값을 직렬화할 때 {@link StringRedisSerializer}를 사용합니다.</p>
     *
     * <h3>사용 예제</h3>
     * <pre>
     * redisTemplate.opsForValue().set("key", "value");
     * String value = (String) redisTemplate.opsForValue().get("key");
     * </pre>
     *
     * @param redisConnectionFactory Redis 연결을 관리하는 {@link RedisConnectionFactory}
     * @return 문자열 기반의 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    /**
     * JSON 직렬화를 지원하는 RedisTemplate을 설정합니다.
     *
     * <p>이 RedisTemplate은 JSON 형식으로 데이터를 저장하고 조회할 수 있도록 설정됩니다.
     * 키는 {@link StringRedisSerializer}를 사용하고, 값은 {@link GenericJackson2JsonRedisSerializer}를 사용하여 JSON 직렬화됩니다.</p>
     *
     * <h3>사용 예제</h3>
     * <pre>
     * redisGenericTemplate.opsForValue().set("user:1", new User("John", 30));
     * User user = (User) redisGenericTemplate.opsForValue().get("user:1");
     * </pre>
     *
     * @param redisConnectionFactory Redis 연결을 관리하는 {@link RedisConnectionFactory}
     * @return JSON 직렬화를 지원하는 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisGenericTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * Jackson ObjectMapper를 설정합니다.
     *
     * <p>이 ObjectMapper는 Java 8 이상의 {@link java.time.LocalDateTime}과 같은 날짜 및 시간 API를 지원하기 위해
     * {@link JavaTimeModule}을 등록하고, 타임스탬프 기반 직렬화를 비활성화합니다.</p>
     *
     * <h3>사용 예제</h3>
     * <pre>
     * ObjectMapper objectMapper = objectMapper();
     * String json = objectMapper.writeValueAsString(new User("John", 30));
     * </pre>
     *
     * @return 날짜 직렬화를 지원하는 ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}