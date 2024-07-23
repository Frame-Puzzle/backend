package com.frazzle.main.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class KakaoInfoDto {
    //카카오 이용자의 고유id
    private Long id;
    //카카오 이용자의 email
    private String email;

    //json파일에서 id와 email을 찾아 매핑
    public KakaoInfoDto(Map<String, Object> attributes) {
        this.id = Long.valueOf(attributes.get("id").toString());
        this.email = attributes.get("email") != null ? attributes.get("email").toString() : "";
    }
}
