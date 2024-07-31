package com.frazzle.main.domain.board.entity;

public enum BoardClearTypeFlag {
    NONE(0),
    PUZZLE_CLEARED(1),
    PUZZLE_GAME_CLEARED(2);

    private final int value;

    BoardClearTypeFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
