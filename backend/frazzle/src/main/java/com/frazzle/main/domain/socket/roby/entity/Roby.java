package com.frazzle.main.domain.socket.roby.entity;

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
public class Roby {

    private int robyId;
    private String imgUrl;
    private int maxPeople;
    private List<RobyUser> robyUserList;
    private Date startTime;
    private Date endTime;
    private RobyUser king;


    @Builder
    public Roby(int robyId, String imgUrl, int maxPeople, List<RobyUser> robyUserList, Date startTime, Date endTime, RobyUser king) {
        this.robyId = robyId;
        this.imgUrl = imgUrl;
        this.maxPeople = maxPeople;
        this.robyUserList = robyUserList;
        this.startTime = startTime;
        this.endTime = endTime;
        this.king = king;
    }

    //객체 생성 후 10분동안만 가능
    public static Roby createRoby(int robyId, int maxPeople, String imgUrl) {
        Date startTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, 10);

        return Roby.builder()
                .robyId(robyId)
                .robyUserList(new ArrayList<>())
                .startTime(startTime)
                .endTime(calendar.getTime())
                .maxPeople(maxPeople)
                .imgUrl(imgUrl)
                .build();
    }

    public void addRobyUser(RobyUser robyUser) {
        robyUserList.add(robyUser);
    }

    public void removeRobyUser(RobyUser robyUser) {
        robyUserList.remove(robyUser);
    }

    public void updateUser(RobyUser robyUser) {
        this.king = robyUser;
    }

}
