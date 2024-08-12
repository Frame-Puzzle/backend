package com.frazzle.main.domain.socket.game.entity;

import com.frazzle.main.domain.user.entity.User;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GamePlayer {
    private int userId;
    private int count;
    private String nickname;
    private String profileImg;


    @Builder
    private GamePlayer(int userId, int count, String nickname, String profileImg) {
        this.userId = userId;
        this.count = count;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }



    public static GamePlayer createGamePlayer(User user) {
        return GamePlayer.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .count(0)
                .build();
    }

    public void upCount() {
        this.count += 1;
    }
}
