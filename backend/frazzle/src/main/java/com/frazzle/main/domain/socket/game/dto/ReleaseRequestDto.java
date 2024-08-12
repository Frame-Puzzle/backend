package com.frazzle.main.domain.socket.game.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ReleaseRequestDto {
    private int puzzleSize;
    private int index;
    private float x;
    private float y;
}