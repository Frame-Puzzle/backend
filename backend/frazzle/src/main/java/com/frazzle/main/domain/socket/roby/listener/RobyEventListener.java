package com.frazzle.main.domain.socket.roby.listener;

import com.frazzle.main.domain.socket.roby.entity.Roby;
import com.frazzle.main.domain.socket.roby.entity.RobyUser;

public interface RobyEventListener {
    void onUserAdded(Roby roby, RobyUser robyUser);
    void onUserRemoved(Roby roby, RobyUser robyUser);
}