package com.frazzle.main.global.openvidu.dto;

import com.frazzle.main.global.openvidu.entity.Openvidu;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpenviduResponseDto {
    private String sessionId;
    private String tokenId;

    @Builder
    private OpenviduResponseDto(String sessionId, String tokenId) {
        this.sessionId = sessionId;
        this.tokenId = tokenId;
    }

    public static OpenviduResponseDto createOpenviduResponseDto(String sessionId, String tokenId) {
        return OpenviduResponseDto.builder()
                .sessionId(sessionId)
                .tokenId(tokenId)
                .build();
    }
}
