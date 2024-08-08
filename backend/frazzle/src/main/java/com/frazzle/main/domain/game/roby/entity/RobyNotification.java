package com.frazzle.main.domain.game.roby.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
public class RobyNotification {
    private String event;
    private Roby roby;

    @Builder
    private RobyNotification(String event, Roby roby) {
        this.event = event;
        this.roby = roby;
    }

    public static RobyNotification createRoomNotification(String event, Roby roby) {
        return RobyNotification.builder()
                .event(event)
                .roby(roby).build();
    }
}

