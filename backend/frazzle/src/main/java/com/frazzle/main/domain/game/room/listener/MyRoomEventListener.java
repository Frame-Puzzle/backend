package com.frazzle.main.domain.game.room.listener;

import com.frazzle.main.domain.game.room.entity.Room;
import com.frazzle.main.domain.game.room.entity.RoomNotification;
import com.frazzle.main.domain.game.room.entity.RoomUser;
import com.frazzle.main.domain.game.room.service.RoomService;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyRoomEventListener implements RoomEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomService roomService;

    @Override
    public void onUserAdded(Room room, RoomUser roomUser) {
        roomService.addUserToRoom(room.getRoomId(), roomUser);
        RoomNotification roomNotification = RoomNotification.createRoomNotification("입장", room);
        messagingTemplate.convertAndSend("/sub/room/" + room.getRoomId(), room);
    }

    @Override
    public void onUserRemoved(Room room, RoomUser roomUser) {
        roomService.removeUserFromRoom(room.getRoomId(), roomUser);
        RoomNotification notification = RoomNotification.createRoomNotification("퇴쟝", room);
        messagingTemplate.convertAndSend("/sub/room/" + room.getRoomId(), notification);
    }

}
