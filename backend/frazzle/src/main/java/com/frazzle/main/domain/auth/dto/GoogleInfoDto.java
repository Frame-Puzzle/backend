package com.frazzle.main.domain.auth.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public class GoogleInfoDto {
    //구글 이용자의 고유 id
    private String id;
    //구글 이용자의 email
    private String email;

    //json 파일에서 id와 email 을 찾아 매핑
    public GoogleInfoDto(JsonNode node) {
        //구글의 경우 sub의 크기가 long보다 커서 string으로 받았음
        String sub = node.get("sub").toString().replace("\"", "");

        this.id = sub;
        //이메일의 경우 따옴표가 존재하여 제거하고 저장
        this.email = node.get("email").toString().replace("\"","");
        log.info("id: {}", this.id);
        log.info("email: {}", this.email);
    }
}
