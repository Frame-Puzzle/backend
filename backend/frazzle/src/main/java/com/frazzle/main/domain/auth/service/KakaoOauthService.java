package com.frazzle.main.domain.auth.service;

import com.frazzle.main.domain.auth.dto.KakaoInfoDto;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOauthService {
    //초기 닉네임 설정을 위한 랜덤 닉네임 생성기
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int NICKNAMELENGTH = 20;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserService userService;

    //프론트에서 가져온 어세스 토큰을 이용해서 카카오에서 정보를 가져옴
    public Map<String, Object> getUserAttributesByToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        //바디와 헤더 설정
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        //uri, 메서드, 헤더 또는 엔티티, 반환 타입
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });

        return response.getBody();
    }


    //가져온 accesstoken을 가공해서 email과 id만 찾아서 가져옴
    public User getUserProfileByToken(String accessToken) {
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(accessToken);
        KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(userAttributesByToken);
        User user = User.builder()
                .nickname(generateRandomNickname())
                .userId(kakaoInfoDto.getId())
                .email(kakaoInfoDto.getEmail())
                .socialType("kakao")
                .build();

        //db에 존재하면 업데이트 아니면 insert
        if(userService.findByUserId(user.getUserId()) !=null) {
            userService.update(user);
        }
        else {
            userService.save(user);
        }
        return user;
    }


    //20글자인 랜덤 닉네임 생성하기
    public static String generateRandomNickname() {
        StringBuilder nickname = new StringBuilder(NICKNAMELENGTH);
        for (int i = 0; i < NICKNAMELENGTH; i++) {
            int index = RANDOM.nextInt(ALPHABET.length());
            nickname.append(ALPHABET.charAt(index));
        }
        return nickname.toString();
    }
}
