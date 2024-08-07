package com.frazzle.main.domain.game.room.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    private int roomId;
    private int maxPeople;
    private List<RoomUser> roomUserList;
    private Date startTime;
    private Date endTime;
    private String king;

    @Builder
    private Room(int roomId, int maxPeople, List<RoomUser> roomUserList, Date startTime, Date endTime) {
        this.roomId = roomId;
        this.maxPeople = maxPeople;
        this.roomUserList = roomUserList;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //객체 생성 후 10분동안만 가능
    public static Room createRoom(int roomId, int maxPeople) {
        Date startTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, 10);

        return Room.builder()
                .roomId(roomId)
                .roomUserList(new ArrayList<>())
                .startTime(startTime)
                .endTime(calendar.getTime())
                .maxPeople(maxPeople)
                .build();
    }

    public void addRoomUser(RoomUser roomUser) {
        roomUserList.add(roomUser);
    }

    public void removeRoomUser(RoomUser roomUser) {
        roomUserList.remove(roomUser);
    }

    public void updateUser(RoomUser roomUser) {
        this.king = roomUser.getNickname();
    }

}
