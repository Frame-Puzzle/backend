package com.frazzle.main.domain.socket.game.entity;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GamePuzzle {
    private float x;
    private float y;
    private int group;

    @Builder
    private GamePuzzle(float x, float y, int group) {
        this.x = x;
        this.y = y;
        this.group = group;
    }

    public static GamePuzzle createGamePuzzle(int group) {
        return GamePuzzle.builder()
                .group(group)
                .build();
    }

    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void updateGroup(int idx) {
        this.group = idx;
    }
}
