package com.frazzle.main.domain.socket.game.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartResponseDto {
    private Boolean flag;

    @Builder
    private StartResponseDto(Boolean flag) {
        this.flag = flag;
    }

    public static StartResponseDto createResponseDto() {
        return StartResponseDto.builder()
                .flag(true)
                .build();
    }
}
