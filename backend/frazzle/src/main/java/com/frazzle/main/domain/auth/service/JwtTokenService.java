package com.frazzle.main.domain.auth.service;

import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Random;

@Service
public class JwtTokenService implements InitializingBean {
    private long accessTokenExpirationInSeconds;
    private Long refreshTokenExpiractionInSeconds;
    private final String secretKey;
    private static Key key;

    //토큰 만료 시간과 시크릿 키
    public JwtTokenService(
            @Value("${jwt.access.token.expiration.seconds}") long accessTokenExpirationInSeconds,
            @Value("${jwt.refresh.token.expiration.seconds}") Long refreshTokenExpiractionInSeconds,
            @Value("${jwt.secret}") String secretKey)
    {
        this.accessTokenExpirationInSeconds = accessTokenExpirationInSeconds * 1000;
        this.refreshTokenExpiractionInSeconds = refreshTokenExpiractionInSeconds * 1000;
        this.secretKey = secretKey;
    }


    //빈 주입 후 실행 메서드
    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = getKeyFromBase64EncodedKey(encodeBase64SecretKey(secretKey));
    }

    //비밀키를 base64로 변환
    private String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //base64를 디코딩하여 바이트 값으로 변환후 키로 저장
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);

        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }

    public String createAccessToken(String payload) {
        return createToken(payload, accessTokenExpirationInSeconds);
    }

    //리프레쉬 토큰의 경우 accessToken보다 수명이 길어
    //랜덤으로 payload를 만들어 보안성 높임
    public String createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return createToken(generatedString, refreshTokenExpiractionInSeconds);
    }

    //토큰 생성 메서드
    //해당 토큰의 주제와 현재 시간을 기준으로 만료 시간을 정함
    //key와 서명 알고리즘을 사용해서 토큰을 만듦
    public String createToken(String payload, long expireLength) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //클라이언트에게 쿠키에 리프레시 토큰 추가하는 메서드
    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Long age = refreshTokenExpiractionInSeconds;
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge(age.intValue());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    //만료 토큰인지 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJwts = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            //현재 날짜를 기준으로 이전인지 확인하기
            return !claimsJwts.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    //페이로드 가져오기 -> 내용
    public String getPayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
