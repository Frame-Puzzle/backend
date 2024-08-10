package com.frazzle.main.domain.socket.game.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ReleaseRequestDto {
    private int currentIdx;
    private int upIdx;
    private int downIdx;
    private int leftIdx;
    private int rightIdx;

}
