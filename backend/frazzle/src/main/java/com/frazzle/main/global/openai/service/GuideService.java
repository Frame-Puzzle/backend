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

    private static final int MAX_TOKEN = 200;
    private static final int MAX_MISSION_NUMBER = 5;

    @Value("${gpt.api.key}")
    private String apiKey;

    @Value("${gpt.api.model}")
    private String gptModel;

    @Value("${gpt.api.url}")
    private String url;


    public String[] generateDescription(GuideRequestDto requestDto) {

        //미션 최대 재생성 개수 5개로 제한
        if(requestDto.getPreMissionList().length>=MAX_MISSION_NUMBER) {
            throw new CustomException(ErrorCode.MAX_GPT_REQUEST);
        }

        RestTemplate restTemplate = new RestTemplate();

        // 요청 본문 준비
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", gptModel); // 올바른 모델 이름 사용

        // messages 배열 구성
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");

        // 키워드 배열을 사용하여 프롬프트 문자열 생성
        StringBuilder promptBuilder = new StringBuilder();

        String combinedKeywords = String.join(", ", requestDto.getKeywordList());
        String prompt = String.format("'%s'가 연관되는 [장소] 또는 [상황] 추천하는 사진 찍기. 설명 형식은 [장소] 에서 [상황]하는 사진 찍기.' 으로 해줘. %d개만 추천 해줘.", combinedKeywords, requestDto.getNum());

        String exceptMissions = String.join(", ", requestDto.getPreMissionList());

        promptBuilder.append(prompt).append("\n").append("뒤에는 제외하고 알려줘. ").append(exceptMissions);


        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", promptBuilder.toString()); // 생성한 프롬프트 문자열 사용

        requestBody.put("messages", List.of(systemMessage, userMessage)); // 불변 리스트 사용
        requestBody.put("max_tokens", MAX_TOKEN); // 최대 토큰 수를 200으로 설정

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseBody = response.getBody();

                log.info(Objects.requireNonNull(response.getBody()).toString());

                // 응답 내용 적절히 추출
                JsonNode messageNode = responseBody.path("choices").get(0).path("message").path("content");


                log.info(messageNode.toString());
                // "guideList" 형태로 결과 반환
                // 응답을 String[]로 변환
                List<String> guides = Arrays.asList(messageNode.asText().replaceAll("[\"\\\\]", "").split("\\.\\s*"));

                //
                List<String> responseGuideList = new ArrayList<>();

                // 홀수 인덱스 항목만 선택
                //짝수의 경우 숫자만 나와서 제외하기
                for (int i = 1; i < guides.size(); i += 2) {
                    responseGuideList.add(guides.get(i));
                }
                return responseGuideList.toArray(new String[0]);
            }
            throw new CustomException(ErrorCode.GPT_BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GPT_BAD_REQUEST);
        }
    }
}