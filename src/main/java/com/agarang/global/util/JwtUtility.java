package com.agarang.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * packageName    : com.agarang.global.util<br>
 * fileName       : JwtUtility.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 24.<br>
 * description    : JWT 토큰 생성 및 검증을 담당하는 유틸리티 클래스입니다.
 * <p>이 클래스는 다음 기능을 제공합니다:</p>
 * <ul>
 *      <li>Access Token 및 Refresh Token 생성</li>
 *      <li>토큰에서 사용자 ID 추출</li>
 *      <li>토큰 유효성 검증</li>
 * </ul>
 * <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24         Fiat_lux            최초생성<br>
 */
@Service
@RequiredArgsConstructor
public class JwtUtility {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    /**
     * JWT 서명 키를 초기화합니다.
     *
     * <p>Base64 인코딩된 비밀 키를 사용하여 HMAC SHA 키를 생성합니다.</p>
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * 사용자 ID를 기반으로 Access Token을 생성합니다.
     *
     * @param userId 사용자 ID
     * @return 생성된 Access Token
     */
    public String generateAccessToken(int userId) {
        return createToken(userId, accessTokenValidity);
    }

    /**
     * 사용자 ID를 기반으로 Refresh Token을 생성합니다.
     *
     * @param userId 사용자 ID
     * @return 생성된 Refresh Token
     */
    public String generateRefreshToken(int userId) {
        return createToken(userId, refreshTokenValidity);
    }

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param userId   사용자 ID
     * @param validity 토큰 유효기간 (밀리초 단위)
     * @return 생성된 JWT 토큰
     */
    private String createToken(int userId, long validity) {
        Claims claims = Jwts.claims();
        claims.setSubject(userId + "");

        Date now = new Date();
        Date expiration = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 ID를 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public String getUserId(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     *
     * @param token 검증할 JWT 토큰
     * @return 유효한 경우 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * JWT 토큰에서 클레임(Claims)을 파싱합니다.
     *
     * @param token JWT 토큰
     * @return 파싱된 Claims 객체
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
