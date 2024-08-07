package com.frazzle.main.domain.game.room.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/*
유저 아이디
닉네임 프로필 사진
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomUser)) return false;
        RoomUser roomUser = (RoomUser) o;
        return getUserId() == roomUser.getUserId() && Objects.equals(getNickname(), roomUser.getNickname()) && Objects.equals(getProfileImg(), roomUser.getProfileImg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getNickname(), getProfileImg());
    }
}
