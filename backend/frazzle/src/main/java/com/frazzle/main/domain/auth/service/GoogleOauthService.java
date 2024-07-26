package com.frazzle.main.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frazzle.main.domain.auth.dto.GoogleInfoDto;
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

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOauthService implements SocialOauthService {

    private final UserService userService;

    private static final String GOOGLE_USER_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    @Value("${google.client-id}")
    String clientId;// 구글 개발자센터에서 발급 받은 Client ID
    @Value("${google.client-secret}")
    String clientSecret; // 구글 개발자센터에서 발급 받은 Client Secret
    @Value("${google.redirect-uri}")
    String redirectUri; // 구글 개발자센터에서 등록한 redirect_uri



    //프론트에서 가져온 access 토큰을 이용해서 구글 정보를 가져옴
    @Override
    public Map<String, Object> getUserAttributesByToken(String token) {

        //구글의 경우 디코딩하지 않은 상태로 오기 때문에 디코딩 해줌
        String code = URLDecoder.decode(token, StandardCharsets.UTF_8);

        //프론트에서 온 인가 토큰을 통해 구글의 accessToken을 가져온다
        RestTemplate restTemplate = new RestTemplate();
        String tokenUri = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("code", code);
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("redirect_uri", redirectUri);
        body.put("grant_type", "authorization_code");

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, body, String.class);

        log.info("response "+response.getBody());

        Map<String, Object> map = new HashMap<>();

        //가져온 결과를 map에 저장한다.
        map.put("result", response.getBody());


        if(response.getStatusCode() == HttpStatus.OK) {
            return map;
        }

        return null;
    }


    //가져온 accesstoken을 가공해서 email과 id만 찾아서 가져옴
    @Override
    public User getUserProfileByToken(String accessToken) {
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(accessToken);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            String result = (String) userAttributesByToken.get("result");

            //accessToken을 통해 가져온 정보를 json형태로 바꿈
            jsonNode = mapper.readTree(result);

            String token = jsonNode.get("access_token").asText();

            log.info("token "+token);

            //가져온 accessToken을 통해 사용자의 정보를 가져옴
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USER_URL, HttpMethod.GET, entity, String.class);

            log.info(response.getBody());

            jsonNode = mapper.readTree(response.getBody());

        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        //가져온 정보를 토대로 구글의 id, email을 가져온다
        GoogleInfoDto googleInfoDto = new GoogleInfoDto(jsonNode);

        log.info(googleInfoDto.toString());

        //유저를 생성한다
        User user = User.createUser(googleInfoDto.getId(), GenerateRandomNickname.generateRandomNickname(), googleInfoDto.getEmail(), "google");

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
