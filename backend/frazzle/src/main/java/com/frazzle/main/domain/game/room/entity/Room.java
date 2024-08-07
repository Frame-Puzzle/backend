package com.frazzle.main.domain.game.room.entity;

import com.frazzle.main.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/*
방 id -> boardId
maxPeople -> 게임의 최대 인원수 == 디렉토리의 최대 인원수
roomUserList -> 방의 유저 리스트
start -> 시작 시간
end -> 끝나는 시간
king -> 반장
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    private int roomId;
    private String imgUrl;
    private int maxPeople;
    private List<RoomUser> roomUserList;
    private Date startTime;
    private Date endTime;
    private RoomUser king;


    @Builder
    public Room(int roomId, String imgUrl, int maxPeople, List<RoomUser> roomUserList, Date startTime, Date endTime, RoomUser king) {
        this.roomId = roomId;
        this.imgUrl = imgUrl;
        this.maxPeople = maxPeople;
        this.roomUserList = roomUserList;
        this.startTime = startTime;
        this.endTime = endTime;
        this.king = king;
    }

    //객체 생성 후 10분동안만 가능
    public static Room createRoom(int roomId, int maxPeople, String imgUrl) {
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
                .imgUrl(imgUrl)
                .build();
    }

    public void addRoomUser(RoomUser roomUser) {
        roomUserList.add(roomUser);
    }

    public void removeRoomUser(RoomUser roomUser) {
        roomUserList.remove(roomUser);
    }

    public void updateUser(RoomUser roomUser) {
        this.king = roomUser;
    }

}
