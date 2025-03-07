package com.agarang.domain.chat_room;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

/**
 * packageName    : com.agarang.domain.chat_room<br>
 * fileName       : WebSocketConfig.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : 웹소켓(WebSocket) 및 STOMP 메시지 브로커를 구성하는 설정 클래스입니다.<br>
 * <p>이 클래스는 STOMP 프로토콜을 지원하는 웹소켓 엔드포인트 및 메시지 브로커를 설정합니다.
 * JSON 메시지 변환을 위한 `MappingJackson2MessageConverter`도 설정합니다.</p><br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * STOMP(WebSocket) 엔드포인트를 등록합니다.
     *
     * <p>클라이언트는 `/agarang` 엔드포인트를 통해 WebSocket 연결을 시도할 수 있습니다.</p>
     *
     * <h3>예제 WebSocket 연결</h3>
     * <pre>
     * const socket = new SockJS("/agarang");
     * const stompClient = Stomp.over(socket);
     * stompClient.connect({}, function (frame) {
     *     console.log("Connected: " + frame);
     * });
     * </pre>
     *
     * @param registry STOMP 엔드포인트를 등록할 레지스트리
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/agarang")
                .setAllowedOriginPatterns("*");
    }

    /**
     * 메시지 브로커를 설정합니다.
     *
     * <p>애플리케이션에서 처리하는 목적지 프리픽스를 `/app`으로 설정하고,
     * 메시지 브로커를 활성화하여 `/topic`, `/queue` 접두사를 가진 목적지를 구독할 수 있도록 합니다.</p>
     *
     * <h3>예제 STOMP 메시지 전송</h3>
     * <pre>
     * stompClient.send("/app/chat-room/1/sendMessage", {}, JSON.stringify({
     *     content: "안녕하세요!",
     *     chattingType: "TEXT"
     * }));
     * </pre>
     *
     * <h3>예제 STOMP 메시지 구독</h3>
     * <pre>
     * stompClient.subscribe("/topic/chat-room/1", function (message) {
     *     console.log("Received:", JSON.parse(message.body));
     * });
     * </pre>
     *
     * @param config 메시지 브로커 레지스트리
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic", "/queue");
    }

    /**
     * JSON 메시지를 처리하는 `MappingJackson2MessageConverter` 빈을 등록합니다.
     *
     * <p>이 설정을 통해 `JavaTimeModule`을 등록하여 `LocalDateTime` 직렬화를 지원하며,
     * 날짜 형식을 `yyyy-MM-dd'T'HH:mm:ss.SSS` 형식으로 변환합니다.</p>
     *
     * @return JSON 메시지 변환기 {@link MappingJackson2MessageConverter}
     */
    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    /**
     * 메시지 변환기를 설정합니다.
     *
     * <p>STOMP 메시지를 처리하기 위해 `StringMessageConverter`와 `MappingJackson2MessageConverter`를 추가합니다.</p>
     *
     * @param messageConverters 사용할 메시지 변환기 목록
     * @return `true`를 반환하여 기본 변환기를 사용하지 않도록 설정
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(new StringMessageConverter());
        messageConverters.add(mappingJackson2MessageConverter());

        return true;
    }
}
