package com.frazzle.main.domain.game.room.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomUser {
    private int userId;
    private String nickname;
    private String profileImg;

    @Builder
    private RoomUser(int userId, String nickname, String profileImg) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public static RoomUser createRoomUser(int userId, String nickname, String profileImg) {
        return RoomUser.builder()
                .userId(userId)
                .nickname(nickname)
                .profileImg(profileImg)
                .build();
    }
}
