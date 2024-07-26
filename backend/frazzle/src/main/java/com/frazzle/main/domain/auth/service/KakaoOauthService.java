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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOauthService implements SocialOauthService {

    private final UserService userService;

    //프론트에서 가져온 access 토큰을 이용해서 카카오에서 정보를 가져옴
    @Override
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
    @Override
    public User getUserProfileByToken(String accessToken) {
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(accessToken);
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
        if(userService.findByLoginUserId(user.getLoginUserId()) !=null) {
            userService.update(user);
        }
        else {
            userService.save(user);
        }
        return user;
    }

}
