package com.frazzle.main.domain.board.dto;

public enum Authority {
    NONE(0),
    EMPTY(1),
    UPDATE_ONLY_ME(2),
    UPDATABLE(3),
    CANNOT_UPDATE(4)
    ;

    private final int value;

    Authority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
