package com.frazzle.main.domain.auth.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public class KakaoInfoDto {
    //카카오 이용자의 고유id
    private Long id;
    //카카오 이용자의 email
    private String email;

    //json파일에서 id와 email을 찾아 매핑
    public KakaoInfoDto(JsonNode node) {
        this.id = Long.valueOf(node.get("id").toString());

        if (node.has("kakao_account")) {
            this.email = node.path("kakao_account").path("email").asText();
        } else {
            this.email = "";
        }
        log.info("id: {}", this.id);
        log.info("email: {}", this.email);
    }
}
