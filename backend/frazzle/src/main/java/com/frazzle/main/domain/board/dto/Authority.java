package com.frazzle.main.domain.board.dto;

/*
0 : default값. 사용하지 않음
1 : 비어있는 상태, 해당 디렉토리에 속한 멤버라면 모두 수정 가능
2 : 자신이 사진을 집어넣은 상태, 오직 본인만 수정 가능
3 : 사진을 넣은 사용자가 사라져서 누구나 수정 가능
4 : 다른 사용자가 사진을 넣어서 수정할 수 없음
 */

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
