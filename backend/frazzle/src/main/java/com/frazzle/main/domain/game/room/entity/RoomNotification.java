package com.frazzle.main.domain.game.room.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
대기방 메시지
1. 이벤트
2. 방 정보
->
방 id -> boardId
maxPeople -> 게임의 최대 인원수 == 디렉토리의 최대 인원수
roomUserList -> 방의 유저 리스트
start -> 시작 시간
end -> 끝나는 시간
king -> 반장
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomNotification {
    private String event;
    private Room room;

    @Builder
    private RoomNotification(String event, Room room) {
        this.event = event;
        this.room = room;
    }

    public static RoomNotification createRoomNotification(String event, Room room) {
        return RoomNotification.builder()
                .event(event)
                .room(room).build();
    }
}

