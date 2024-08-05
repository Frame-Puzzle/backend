package com.frazzle.main.domain.notification.entity;

//0과 1은 수락, 거절 가능
//2, 3은 그냥 확인만 가능
//0은 디렉토리에 유저 초대
//1은 퍼즐판 삭제 투표
//2는 퍼즐판 완성
//3은 게임방 생성
public enum NotificationTypeFlag {
    INVITE_PEOPLE(0),
    VOTE_BOARD(1),
    COMPLETE_BOARD(2),
    CREATE_GAME_ROOM(3)
    ;

    private final int value;

    NotificationTypeFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
