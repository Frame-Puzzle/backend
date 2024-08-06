package com.frazzle.main.domain.game.room.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RoomService {
    private final Map<Integer, Set<String>> roomUsers = new HashMap<>();

    //유저를 방에 추가
    public void addUserToRoom(int roomId, String username) {
        roomUsers.computeIfAbsent(roomId, k -> new HashSet<>()).add(username);
    }

    //유저를 방에서 제거
    public void removeUserFromRoom(int roomId, String username) {
        Set<String> users = roomUsers.get(roomId);
        if (users != null) {
            users.remove(username);
            if (users.isEmpty()) {
                roomUsers.remove(roomId);
            }
        }
    }

    //방에서 유저들 체크
    public Set<String> getUsersInRoom(int roomId) {
        return roomUsers.getOrDefault(roomId, new HashSet<>());
    }
}
