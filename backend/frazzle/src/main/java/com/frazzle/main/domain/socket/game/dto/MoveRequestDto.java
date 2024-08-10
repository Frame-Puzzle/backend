package com.frazzle.main.domain.socket.game.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class MoveRequestDto {
    private int index;
    private float x;
    private float y;
}
