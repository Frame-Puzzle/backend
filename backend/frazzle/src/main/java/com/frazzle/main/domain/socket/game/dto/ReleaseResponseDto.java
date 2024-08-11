package com.frazzle.main.domain.socket.game.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReleaseResponseDto {
    private int index;
    private float x;
    private float y;

    @Builder
    private ReleaseResponseDto(int index, float x, float y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    public static ReleaseResponseDto createResponseDto(int index, float x, float y) {
        return ReleaseResponseDto.builder()
                .index(index)
                .x(x)
                .y(y)
                .build();
    }
}
