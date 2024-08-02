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

    public String createSession() {

        RestTemplate restTemplate = new RestTemplate();

        String url = openviduUrl + "/api/sessions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.encodeBase64String(("OPENVIDUAPP:" + secret).getBytes()));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(new HashMap<>(), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return response.getBody();
    }

    public String createToken(String sessionId) {

        RestTemplate restTemplate = new RestTemplate();

        String url = openviduUrl + "/api/tokens";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.encodeBase64String(("OPENVIDUAPP:" + secret).getBytes()));

        Map<String, String> body = new HashMap<>();
        body.put("session", sessionId);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return response.getBody();
    }
}