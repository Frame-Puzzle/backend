package com.frazzle.main.domain.auth.controller;

import com.frazzle.main.domain.auth.dto.OauthRequestDto;
import com.frazzle.main.domain.auth.dto.OauthResponseDto;
import com.frazzle.main.domain.auth.dto.RefreshTokenResponseDto;
import com.frazzle.main.domain.auth.service.OauthService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OauthController {
    private final OauthService oauthService;

    @PostMapping("/login/oauth/{provider}")
    public ResponseEntity<OauthResponseDto> login(@PathVariable String provider, @RequestBody OauthRequestDto oauthRequestDto, HttpServletResponse response) {

        //카카오, 구글로그인을 구분해서 받기
        switch (provider) {
            case "kakao":
                String accessToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), response);

                OauthResponseDto oauthResponseDto = OauthResponseDto.builder()
                        .accessToken(accessToken)
                        .build();

                return ResponseEntity.ok(oauthResponseDto);
        }

        return ResponseEntity.notFound().build();
    }

    //리프레시 토큰을 보고 있으면 에세스 토큰을 만들고 없으면 예외 처리하기
    @PostMapping("/token/refresh")
    public ResponseEntity<RefreshTokenResponseDto> tokenRefresh(HttpServletRequest request) {
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        Cookie[] list = request.getCookies();

        if(list==null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token")).collect(Collectors.toList()).get(0);

        if(refreshTokenCookie == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String accessToken = oauthService.refreshAccessToken(refreshTokenCookie.getValue());
        refreshTokenResponseDto.setAccessToken(accessToken);
        return ResponseEntity.ok(refreshTokenResponseDto);
    }
}
