package com.frazzle.main.domain.socket.game.dto;

import com.frazzle.main.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoveResponseDto {
    private int idx;
    private String nickname;
    private int userId;


    @Builder
    private MoveResponseDto(int idx, String nickname, int userId) {
        this.idx = idx;
        this.nickname = nickname;
        this.userId = userId;
    }



    public static MoveResponseDto createResponseDto(int idx, User user) {
        return MoveResponseDto.builder()
                .idx(idx)
                .nickname(user.getNickname())
                .userId(user.getUserId())
                .build();
    }
}
