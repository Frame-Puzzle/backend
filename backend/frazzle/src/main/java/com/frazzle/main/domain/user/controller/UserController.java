package com.frazzle.main.domain.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.security.auth.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody TokenRequest tokenRequest) {
        String accessToken = tokenRequest.getToken();
        RestTemplate restTemplate = new RestTemplate();

        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, String.class);

        // JSON 응답에서 이메일 추출
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        String email = null;

        try {
            rootNode = objectMapper.readTree(response.getBody());
            JsonNode kakaoAccountNode = rootNode.path("kakao_account");
            email = kakaoAccountNode.path("email").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        User user = new User(email, "kakao", tokenRequest.getToken());
        userService.login(user);

        return ResponseEntity.ok(response.getBody());
    }
}


