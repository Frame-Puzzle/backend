package com.frazzle.main.domain.game.room.service;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.game.room.entity.Room;
import com.frazzle.main.domain.game.room.entity.RoomUser;
import com.frazzle.main.domain.game.room.listener.RoomEventListener;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RoomService {
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    private final Map<Integer, Room> roomList = new HashMap<>();
    private final List<RoomEventListener> listeners = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final BoardRepository boardRepository;
    private final DirectoryRepository directoryRepository;

    @Transactional
    public void createRoom(int boardId) {

        if (!roomList.containsKey(boardId)) {
//            Board board = boardRepository.findByBoardId(boardId).orElseThrow(
//                    () -> new CustomException(ErrorCode.NOT_EXIST_BOARD)
//            );
//
//            Directory directory =  directoryRepository.findB(4).orElseThrow(
//                    () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
//            );

            Directory directory = directoryRepository.findByBoardId(boardId);

            Room room = Room.createRoom(boardId, directory.getPeopleNumber());
            roomList.put(boardId, room);
            long delay = room.getEndTime().getTime() - System.currentTimeMillis();
            scheduler.schedule(() -> removeRoom(boardId), delay, TimeUnit.MILLISECONDS);
        }
    }

    private void removeRoom(int roomId) {
        Room room = roomList.remove(roomId);
        if (room != null) {
            for (RoomUser user : room.getRoomUserList()) {
                notifyUserRemoved(room, user);
            }
        }
    }

    public void addUserToRoom(int boardId, RoomUser roomUser) {

        Room room = roomList.get(boardId);

        if (room == null) {
            createRoom(boardId);
            room = roomList.get(boardId);
            room.updateUser(roomUser);
        }

        // 유저 리스트에서 닉네임 비교하여 포함 여부 확인
        boolean userExists = room.getRoomUserList().stream()
                .anyMatch(existingUser -> existingUser.getNickname().equals(roomUser.getNickname()));

        if (userExists) {
            return;
        }

        notifyUserAdded(room, roomUser);
        room.addRoomUser(roomUser);
    }

    public void removeUserFromRoom(int roomId, RoomUser roomUser) {

        Room room = roomList.get(roomId);
        RoomUser removeUser = null;
        if (room != null) {
            List<RoomUser> roomUserList = room.getRoomUserList();
            for (RoomUser user : roomUserList) {
                if (roomUser.getNickname().equals(user.getNickname())) {
                    removeUser = roomUser;
                    break;
                }
            }

            roomUserList.remove(removeUser);
            notifyUserRemoved(room, removeUser);

            if(room.getKing().equals(roomUser.getNickname())) {
                room.updateUser(room.getRoomUserList().get(0));
            }


            if (room.getRoomUserList().isEmpty()) {
                roomList.remove(roomId);
            }
        }
    }

    public List<RoomUser> getUsersInRoom(int roomId) {
        Room room = roomList.get(roomId);
        if (room != null) {
            return room.getRoomUserList();
        }
        return Collections.emptyList();
    }

    public Date getRoomStartTime(int roomId) {
        Room room = roomList.get(roomId);
        if (room != null) {
            return room.getStartTime();
        }
        return null;
    }

    public Date getRoomEndTime(int roomId) {
        Room room = roomList.get(roomId);
        if (room != null) {
            return room.getEndTime();
        }
        return null;
    }

    public void addRoomEventListener(RoomEventListener listener) {
        listeners.add(listener);
    }

    public void removeRoomEventListener(RoomEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyUserAdded(Room room, RoomUser roomUser) {
        for (RoomEventListener listener : listeners) {
            listener.onUserAdded(room, roomUser);
        }
    }

    private void notifyUserRemoved(Room room, RoomUser user) {
        for (RoomEventListener listener : listeners) {
            listener.onUserRemoved(room, user);
        }
    }

    public Room getRoom(int roomId) {
        return roomList.get(roomId);
    }

}
