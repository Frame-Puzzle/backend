package com.frazzle.main.domain.auth.service;

import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final KakaoOauthService kakaoOauthService;
    private final GoogleOauthService googleOauthService;

    //카카오 로그인
    public Map<String,String> loginWithKakao(String code, HttpServletResponse response) {
        User user = kakaoOauthService.getUserProfileByToken(code);
        return getTokens(user.getUserId(), response);
    }

    public Map<String,String> loginWithGoogle(String code, HttpServletResponse response) {
        User user = googleOauthService.getUserProfileByToken(code);
        return getTokens(user.getUserId(), response);
    }

    public Map<String,String> getTokens(int id, HttpServletResponse response) {
        //사용자 id가지고 access,refresh 토큰 만들기
        final String accessToken = jwtTokenService.createAccessToken(String.valueOf(id));
        final String refreshToken = jwtTokenService.createRefreshToken();

        //사용자 리프레시 토큰 업데이트
        User user = userService.findUser(id);

        userService.updateRefreshToken(user, refreshToken);

        jwtTokenService.addRefreshTokenToCookie(refreshToken, response);

        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);

        return tokenMap;
    }

    //리프레시 토큰으로 accessToken만들기
    public String refreshAccessToken(String refreshToken) {
        User user = userService.findByRefreshToken(refreshToken);
        if(user == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if(!jwtTokenService.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return jwtTokenService.createAccessToken(user.getLoginUserId());
    }
}
