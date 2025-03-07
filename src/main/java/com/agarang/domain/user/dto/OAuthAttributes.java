package com.agarang.domain.user.dto;

import com.agarang.domain.user.entity.Sex;
import com.agarang.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String gender;
    private LocalDate birth;
    private String provider;
    private String providerId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String name, String email, String provider, String providerId,
                           String gender, String birthyear, String birthday) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.gender = gender;
        this.birth = parseBirthDate(birthyear, birthday);
    }

    private LocalDate parseBirthDate(String birthyear, String birthday) {
        try {
            if (birthyear != null && birthday != null) {
                String birthDateString = birthyear + "-" + birthday.substring(0, 2) + "-" + birthday.substring(2, 4);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(birthDateString, formatter);
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuthAttributes.builder()
                .name((String) kakaoAccount.get("name"))
                .email((String) kakaoAccount.get("email"))
                .gender((String) kakaoAccount.get("gender"))
                .birthyear((String) kakaoAccount.get("birthyear"))
                .birthday((String) kakaoAccount.get("birthday"))
                .provider("KAKAO")
                .providerId(String.valueOf(attributes.get("id")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .sex(Sex.valueOf(gender.toUpperCase()))
                .birth(birth)
                .createdAt(LocalDateTime.now())
                .providerId(providerId)
                .build();
    }

}
