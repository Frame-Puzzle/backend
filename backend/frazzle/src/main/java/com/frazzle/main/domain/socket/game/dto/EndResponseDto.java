package com.frazzle.main.domain.socket.game.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EndResponseDto {
    private int time;
    private String nickname;

    @Builder
    private EndResponseDto(int time, String nickname) {
        this.time = time;
        this.nickname = nickname;
    }

    public static EndResponseDto createEndResponseDto(int time, String nickname) {
        return EndResponseDto.builder()
                .time(time)
                .nickname(nickname)
                .build();
    }
}
