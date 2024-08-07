package com.frazzle.main.domain.game.room.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

