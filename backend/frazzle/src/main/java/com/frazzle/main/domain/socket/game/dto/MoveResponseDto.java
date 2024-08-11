package com.frazzle.main.domain.socket.game.dto;

import com.frazzle.main.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoveResponseDto {
    private int group;
    private String nickname;
    private int userId;


    @Builder
    private MoveResponseDto(int group, String nickname, int userId) {
        this.group = group;
        this.nickname = nickname;
        this.userId = userId;
    }



    public static MoveResponseDto createResponseDto(int group, User user) {
        return MoveResponseDto.builder()
                .group(group)
                .nickname(user.getNickname())
                .userId(user.getUserId())
                .build();
    }
}
