package com.agarang.global.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.agarang.global.ai<br>
 * fileName       : OpenAiService.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-13<br>
 * description    :  open ai service 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.13          Fiat_lux           최초생성<br>
 * 25.02.20          okeio              별도의 api key를 가지는 webClient 추가<br>
 * <br>
 */
@Service
public class OpenAiService {
    private final WebClient webClient;
    private final WebClient webClientForVoiceRecognition;

    private final String apiKey;
    private final String apiKeyForVoiceRecognition;

    /**
     * OpenAiService 생성자입니다.
     *
     * <p>일반적인 GPT API와 음성 인식을 위한 API를 각각 다른 API 키로 설정할 수 있도록 `WebClient`를 두 개 생성합니다.</p>
     *
     * @param webClientBuilder          `WebClient` 빌더
     * @param apiKey                    일반 GPT API 요청을 위한 OpenAI API 키
     * @param apiKeyForVoiceRecognition 음성 인식을 위한 OpenAI API 키
     */
    public OpenAiService(
            WebClient.Builder webClientBuilder,
            @Value("${openai.api-key}") String apiKey,
            @Value("${openai.api-key2}") String apiKeyForVoiceRecognition) {
        this.apiKey = apiKey;
        this.apiKeyForVoiceRecognition = apiKeyForVoiceRecognition;

        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        this.webClientForVoiceRecognition = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + apiKeyForVoiceRecognition)
                .build();
    }

    /**
     * OpenAI의 GPT 모델에 프롬프트를 전송하여 응답을 받습니다.
     *
     * <p>일반적인 텍스트 기반의 프롬프트를 전송하는 경우 이 메서드를 사용합니다.</p>
     *
     * <h3>예제</h3>
     * <pre>
     * String response = openAiService.sendPrompt("오늘 날씨 어때?").block();
     * System.out.println(response);
     * </pre>
     *
     * @param prompt 사용자 입력 프롬프트
     * @return OpenAI의 응답을 포함하는 {@link Mono<String>}
     */
    public Mono<String> sendPrompt(String prompt) {
        return sendPromptWithClient(prompt, webClient);
    }

    /**
     * 음성 인식 관련 프롬프트를 OpenAI에 전송하여 응답을 받습니다.
     *
     * <p>음성 데이터를 텍스트로 변환하거나 음성과 관련된 AI 모델을 사용할 경우 이 메서드를 사용합니다.</p>
     *
     * <h3>예제</h3>
     * <pre>
     * String response = openAiService.sendPromptForVoiceRecognition("음성을 분석해주세요.").block();
     * System.out.println(response);
     * </pre>
     *
     * @param prompt 사용자 입력 프롬프트
     * @return OpenAI의 응답을 포함하는 {@link Mono<String>}
     */
    public Mono<String> sendPromptForVoiceRecognition(String prompt) {
        return sendPromptWithClient(prompt, webClientForVoiceRecognition);
    }

    /**
     * 지정된 WebClient를 사용하여 OpenAI API에 프롬프트를 전송하고 응답을 받습니다.
     *
     * <p>이 메서드는 `sendPrompt` 및 `sendPromptForVoiceRecognition`에서 내부적으로 사용되며,
     * 요청하는 WebClient를 선택적으로 지정할 수 있도록 설계되었습니다.</p>
     *
     * @param prompt    사용자 입력 프롬프트
     * @param webClient 사용할 WebClient 객체 (일반 요청 또는 음성 인식 요청용)
     * @return OpenAI의 응답을 포함하는 {@link Mono<String>}
     */
    public Mono<String> sendPromptWithClient(String prompt, WebClient webClient) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .map(response -> {
                    if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                        return response.getChoices().get(0).getMessage().getContent();
                    }
                    return "응답이 없습니다.";
                });
    }
}
