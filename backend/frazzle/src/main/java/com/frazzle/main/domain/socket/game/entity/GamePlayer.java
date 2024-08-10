package com.frazzle.main.domain.socket.game.entity;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GamePlayer {
    private int userId;
    private int count;

    @Builder
    private GamePlayer(int userId, int count) {
        this.userId = userId;
        this.count = count;
    }

    public static GamePlayer createGamePlayer(int userId) {
        return GamePlayer.builder()
                .userId(userId)
                .count(0)
                .build();
    }

    public void upCount() {
        this.count += 1;
    }
}
