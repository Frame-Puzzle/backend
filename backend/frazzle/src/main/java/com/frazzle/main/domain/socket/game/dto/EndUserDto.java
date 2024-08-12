package com.frazzle.main.domain.socket.game.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EndUserDto {
    private int userId;
    private String nickname;
    private int count;

    @Builder
    private EndUserDto(int userId, String nickname, int count) {
        this.userId = userId;
        this.nickname = nickname;
        this.count = count;
    }

    public static  EndUserDto createEndUserDto(String nickname, int count, int userId) {
        return EndUserDto.builder()
                .userId(userId)
                .nickname(nickname)
                .count(count)
                .build();
    }
}
