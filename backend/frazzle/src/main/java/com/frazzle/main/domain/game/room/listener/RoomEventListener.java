package com.frazzle.main.domain.game.room.listener;

import com.frazzle.main.domain.game.room.entity.Room;
import com.frazzle.main.domain.game.room.entity.RoomUser;
import com.frazzle.main.global.models.UserPrincipal;

public interface RoomEventListener {
    void onUserAdded(Room room, RoomUser roomUser);
    void onUserRemoved(Room room, RoomUser roomUser);
}