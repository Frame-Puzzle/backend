package com.frazzle.main.domain.socket.roby.entity;

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
public class RobyUser {
    private int userId;
    private String nickname;
    private String profileImg;

    @Builder
    private RobyUser(int userId, String nickname, String profileImg) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public static RobyUser createRoomUser(int userId, String nickname, String profileImg) {
        return RobyUser.builder()
                .userId(userId)
                .nickname(nickname)
                .profileImg(profileImg)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RobyUser)) return false;
        RobyUser robyUser = (RobyUser) o;
        return getUserId() == robyUser.getUserId() && Objects.equals(getNickname(), robyUser.getNickname()) && Objects.equals(getProfileImg(), robyUser.getProfileImg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getNickname(), getProfileImg());
    }
}
