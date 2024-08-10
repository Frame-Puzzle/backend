package com.frazzle.main.domain.socket.game.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class MoveResponseDto {
    private int index;
    private float x;
    private float y;

    @Builder
    private MoveResponseDto(int index, float x, float y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    public static MoveResponseDto createResponseDto(int index, float x, float y) {
        return MoveResponseDto.builder()
                .index(index)
                .x(x)
                .y(y)
                .build();
    }
}
