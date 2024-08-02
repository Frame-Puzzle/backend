package com.frazzle.main.global.openai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.openai.dto.GuideRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class GuideService {

    @Value("${gpt.api.key}")
    private String apiKey;

    public String[] generateDescription(GuideRequestDto requestDto) {

    RestTemplate restTemplate = new RestTemplate();

    // 올바른 엔드포인트
    String url = "https://api.openai.com/v1/chat/completions"; 

    // 요청 본문 준비
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", "gpt-3.5-turbo"); // 올바른 모델 이름 사용

    // messages 배열 구성
    Map<String, String> systemMessage = new HashMap<>();
    systemMessage.put("role", "system");
    systemMessage.put("content", "You are a helpful assistant.");

    // 키워드 배열을 사용하여 프롬프트 문자열 생성
    StringBuilder promptBuilder = new StringBuilder();

    // 키워드 리스트를 하나의 문자열로 결합
    String combinedKeywords = String.join(", ", requestDto.getKeywordList());
    
    //프롬포트
    String prompt = String.format("'%s'가 연관되는 [장소] 또는 [상황] 추천하는 사진 찍기. 설명 형식은 [장소] 에서 [상황]하는 사진 찍기.' 으로 해줘. %d개만 추천 해줘.", combinedKeywords, requestDto.getNum());

    Map<String, String> userMessage = new HashMap<>();
    userMessage.put("role", "user");
    userMessage.put("content", prompt); // 생성한 프롬프트 문자열 사용

    requestBody.put("messages", List.of(systemMessage, userMessage)); // 불변 리스트 사용
    requestBody.put("max_tokens", 200); // 예시로 최대 토큰 수를 100으로 설정

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + apiKey);
    headers.set("Content-Type", "application/json");

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseBody = response.getBody();

                // 응답 내용 적절히 추출
                JsonNode messageNode = responseBody.path("choices").get(0).path("message").path("content");

                if(requestDto.getNum()==1) {
                    String replaceMessage = messageNode.asText().replace("'", "");

                    return replaceMessage.split("\\.\\s*");
                }

                // "guideList" 형태로 결과 반환
                // 응답을 String[]로 변환
                List<String> guides = Arrays.asList(messageNode.asText().split("\\.\\s*"));
                List<String> oddGuides = new ArrayList<>();

                // 홀수 인덱스 항목만 선택
                for (int i = 1; i < guides.size(); i += 2) {
                    oddGuides.add(guides.get(i));
                }

                return oddGuides.toArray(new String[0]);
            }
            throw new CustomException(ErrorCode.GPT_BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GPT_BAD_REQUEST);
        }
    }
}