package com.frazzle.main.global.openvidu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.openvidu.dto.OpenviduRequestDto;
import com.frazzle.main.global.openvidu.dto.OpenviduResponseDto;
import com.frazzle.main.global.openvidu.entity.Openvidu;
import com.frazzle.main.global.openvidu.repository.OpenviduRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenViduService {

    private final OpenviduRepository openviduRepository;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


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

    private void sendDeleteRequest(String endpoint) {
        String url = openviduUrl + endpoint;
        HttpEntity<Void> request = new HttpEntity<>(createHeaders());
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Session not found: " + endpoint);
            } else {
                throw e;
            }
        }
    }

    public boolean isSessionActive(String sessionId) {
        String url = openviduUrl + "/api/sessions";

        log.info("세션 아직 열려있는지 확인 메서드");
        log.info(sessionId);

        try {
            HttpEntity<Map<String, String>> request = new HttpEntity<>(createHeaders());
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body != null) {
                    List<Map<String, Object>> sessions = (List<Map<String, Object>>) body.get("content");
                    return sessions.stream().anyMatch(session -> sessionId.equals(session.get("sessionId")));
                }
            }
        } catch (HttpClientErrorException e) {
            // Handle error
            System.err.println("Error fetching session information: " + e.getMessage());
        }

        return false;
    }

    public OpenviduResponseDto entryChat(OpenviduRequestDto requestDto) {
        String sessionId = createSession(requestDto);
        String tokenId = createToken(sessionId);

        return OpenviduResponseDto.createOpenviduResponseDto(sessionId, tokenId);
    }

    //세션 방만들기
    public String createSession(OpenviduRequestDto requestDto) {
        String sessionId;

        Optional<Openvidu> openvidu = openviduRepository.findByBoardId(requestDto.getBoardId());
        if(openvidu.isPresent()) {
            sessionId = openvidu.get().getSessionId();

            log.info("isPresent");
            log.info(sessionId);

            if(isSessionActive(sessionId)) {
                log.info("true");
                log.info(sessionId);

                return sessionId;
            }
            sessionId = sendPostRequest("/api/sessions", new HashMap<>());

            log.info(sessionId);

            try {
                String jsonResponse = sendPostRequest("/api/sessions", new HashMap<>());
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                sessionId = jsonNode.get("id").asText();

                log.info("end");
                log.info(sessionId);
            } catch (Exception e) {
                // 예외 처리 로직
                throw new CustomException(ErrorCode.DENIED_OPENVIDU);
            }

            openvidu.get().updateSession(sessionId);
            openviduRepository.save(openvidu.get());

            log.info("false");
            log.info(sessionId);

            return  sessionId;
        }

        try {
            String jsonResponse = sendPostRequest("/api/sessions", new HashMap<>());
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            sessionId = jsonNode.get("id").asText();

            log.info("end");
            log.info(sessionId);

            Openvidu saveOpenvidu = Openvidu.createOpenvidu(requestDto);
            saveOpenvidu.updateSession(sessionId);
            openviduRepository.save(saveOpenvidu);
            return sessionId;
        } catch (Exception e) {
            // 예외 처리 로직
            throw new CustomException(ErrorCode.DENIED_OPENVIDU);
        }

    }

    //만든 세션방의 각 사람 마다 토큰 값 생성하기
    public String createToken(String sessionId) {
        Map<String, String> body = new HashMap<>();
        body.put("session", sessionId);

        try {
            String jsonResponse = sendPostRequest("/api/tokens", body);
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            log.info(jsonNode.toString());

            String tokenId = jsonNode.get("id").asText();

            log.info("end");
            return tokenId;
        } catch (Exception e) {
            // 예외 처리 로직
            throw new CustomException(ErrorCode.DENIED_OPENVIDU);
        }

    }

    // 세션 강제로 종료
    public void deleteSession(String sessionId) {
        String endpoint = "/api/sessions/" + sessionId;
        sendDeleteRequest(endpoint);
        log.info("Session deleted: " + sessionId);
    }
}