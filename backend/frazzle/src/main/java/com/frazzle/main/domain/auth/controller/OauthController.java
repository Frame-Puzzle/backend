package com.frazzle.main.domain.auth.controller;

import com.frazzle.main.domain.auth.dto.OauthRequestDto;
import com.frazzle.main.domain.auth.dto.OauthResponseDto;
import com.frazzle.main.domain.auth.dto.RefreshTokenResponseDto;
import com.frazzle.main.domain.auth.service.OauthService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.utils.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OauthController {
    private final OauthService oauthService;

    @Operation(summary = "소셜 로그인", description = "Kakao 또는 Google을 통한 OAuth 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "404", description = "로그인에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    @PostMapping("/login/oauth/{provider}")
    public ResponseEntity<ResultDto> login(@PathVariable String provider, @RequestBody OauthRequestDto oauthRequestDto, HttpServletResponse response) {

//        String accessToken;
        Map<String, String> tokenMap;
        OauthResponseDto oauthResponseDto;

        //카카오, 구글로그인을 구분해서 받기
        log.info(provider);
        switch (provider) {
            case "kakao":
                tokenMap = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), response);

                oauthResponseDto = OauthResponseDto.builder()
                        .accessToken(tokenMap.get("accessToken"))
                        .refreshToken(tokenMap.get("refreshToken"))
                        .build();

                log.info(oauthResponseDto.toString());

                return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "로그인에 성공했습니다.", oauthResponseDto));

            case "google":
                tokenMap = oauthService.loginWithGoogle(oauthRequestDto.getAccessToken(), response);

                oauthResponseDto = OauthResponseDto.builder()
                        .accessToken(tokenMap.get("accessToken"))
                        .refreshToken(tokenMap.get("refreshToken"))
                        .build();

                log.info(oauthResponseDto.toString());

                return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "로그인에 성공했습니다.", oauthResponseDto));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultDto.res(HttpStatus.NOT_FOUND.value(), "로그인에 실패했습니다."));
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 재발급 했습니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 리프레시 토큰입니다.",
                    content = @Content(schema = @Schema(implementation = ResultDto.class)))
    })
    //리프레시 토큰을 보고 있으면 에세스 토큰을 만들고 없으면 예외 처리하기
    @PostMapping("/token/refresh")
    public ResponseEntity<ResultDto> tokenRefresh(HttpServletRequest request) {

        Cookie[] list = request.getCookies();

        if(list==null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token")).collect(Collectors.toList()).get(0);

        if(refreshTokenCookie == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String accessToken = oauthService.refreshAccessToken(refreshTokenCookie.getValue());

        RefreshTokenResponseDto refreshTokenResponseDto = RefreshTokenResponseDto.builder()
                .accessToken(accessToken)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(ResultDto.res(HttpStatus.OK.value(), "성공적으로 재발급 했습니다.", refreshTokenResponseDto));
    }
}
