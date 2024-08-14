package com.frazzle.main.domain.socket.game.entity;

import com.frazzle.main.domain.socket.roby.entity.RobyUser;
import com.frazzle.main.domain.user.entity.User;
import lombok.*;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GamePlayer {
    private int userId;
    private String nickname;
    private String profileImg;


    @Builder
    private GamePlayer(int userId, String nickname, String profileImg) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public static GamePlayer createGamePlayer(RobyUser user) {
        return GamePlayer.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .build();
    }

    public static GamePlayer createGamePlayer(User user) {
        return GamePlayer.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GamePlayer)) return false;
        GamePlayer that = (GamePlayer) o;
        return getUserId() == that.getUserId() && Objects.equals(getNickname(), that.getNickname()) && Objects.equals(getProfileImg(), that.getProfileImg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getNickname(), getProfileImg());
    }
}
