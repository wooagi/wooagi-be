package com.agarang.global.ai;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * packageName    : com.agarang.global.ai<br>
 * fileName       : OpenAiResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-13<br>
 * description    :  Open ai response dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.13          Fiat_lux            최초생성<br>
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiResponse {
    private List<Choice> choices;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Message message;

    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String role;
        private String content;

    }
}