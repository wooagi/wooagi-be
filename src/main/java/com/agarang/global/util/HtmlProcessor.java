package com.agarang.global.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Objects;

/**
 * packageName    : com.agarang.global.util<br>
 * fileName       : HtmlProcessor.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 13.<br>
 * description    : HTML 문자열을 일반 텍스트로 변환하는 유틸리티 클래스입니다.
 * <p>이 클래스는 HTML 태그를 제거하고 순수한 텍스트만 추출하는 기능을 제공합니다.</p>
 * <p>내부적으로 <a href="https://jsoup.org/">jsoup</a> 라이브러리를 사용하여 HTML을 파싱하고, 텍스트만 반환합니다.</p><br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.13         Fiat_lux            최초생성<br>
 */
public class HtmlProcessor {

    /**
     * HTML 문자열을 일반 텍스트로 변환합니다.
     *
     * <p>이 메서드는 HTML 태그를 제거하고 텍스트만 추출합니다.
     * 입력이 null이거나 빈 문자열이면 빈 문자열을 반환합니다.</p>
     *
     * @param htmlContent 변환할 HTML 문자열
     * @return HTML 태그가 제거된 일반 텍스트 문자열
     */
    public static String convertHtmlToPlainText(String htmlContent) {
        if (Objects.isNull(htmlContent) || htmlContent.isEmpty()) {
            return "";
        }

        Document document = Jsoup.parse(htmlContent);
        return document.text();
    }

    private HtmlProcessor() {
    }
}