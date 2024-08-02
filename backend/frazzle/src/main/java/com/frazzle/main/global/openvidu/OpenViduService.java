package com.frazzle.main.global.openvidu;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenViduService {

    @Value("${openvidu.url}")
    private String openviduUrl;

    @Value("${openvidu.secret}")
    private String secret;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.encodeBase64String(("OPENVIDUAPP:" + secret).getBytes()));
        return headers;
    }

    private String sendPostRequest(String endpoint, Map<String, String> body) {
        RestTemplate restTemplate = new RestTemplate();
        String url = openviduUrl + endpoint;
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    //세션 방만들기
    public String createSession() {
        return sendPostRequest("/api/sessions", new HashMap<>());
    }


    //만든 세션방의 각 사람 마다 토큰 값 생성하기
    public String createToken(String sessionId) {
        Map<String, String> body = new HashMap<>();
        body.put("session", sessionId);
        return sendPostRequest("/api/tokens", body);
    }
}