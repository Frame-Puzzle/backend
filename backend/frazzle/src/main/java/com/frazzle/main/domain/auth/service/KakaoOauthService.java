package com.frazzle.main.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frazzle.main.domain.auth.dto.KakaoInfoDto;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.utils.GenerateRandomNickname;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOauthService implements SocialOauthService {

    private final UserService userService;

    @Value("${kakao.client-id}")
    String clientId;// 카카오 rest api 키
    @Value("${kakao.redirect-uri}")
    String redirectUri; // 카카오에 등록한 redirect_uri

    //프론트에서 가져온 access 토큰을 이용해서 카카오에서 정보를 가져옴
    @Override
    public Map<String, Object> getUserAttributesByToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String getKakaoAccessTokenUri = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("grant_type","authorization_code");
        body.put("client_id",clientId);
        body.put("redirect_uri",redirectUri);
        body.put("code", code);

        ResponseEntity<String> response = restTemplate.postForEntity(getKakaoAccessTokenUri, body, String.class);

        String responseBody = response.getBody();

        String accessToken = null;

        try {
            // JSON 응답 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // access_token 추출
            accessToken = rootNode.path("access_token").asText();

            log.info(accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        restTemplate = new RestTemplate();

        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        //바디와 헤더 설정
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        //uri, 메서드, 헤더 또는 엔티티, 반환 타입
        ResponseEntity<Map<String, Object>> accessTokenResponse = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {
        });

        return accessTokenResponse.getBody();
    }


    //가져온 accesstoken을 가공해서 email과 id만 찾아서 가져옴
    @Override
    public User getUserProfileByToken(String code) {
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(code);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(mapper.writeValueAsString(userAttributesByToken));
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(jsonNode);

        User user = User.createUser(kakaoInfoDto.getId(), GenerateRandomNickname.generateRandomNickname(), kakaoInfoDto.getEmail(), "kakao");

        //db에 존재하면 업데이트 아니면 insert
        if(userService.findByUserId(user.getUserId()) !=null) {

            User findUser = userService.findByUserId(user.getUserId());

            userService.updateUser(user, findUser);

            return user;
        }
        return userService.save(user);
    }

}
