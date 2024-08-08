package com.frazzle.main.domain.game.roby.listener;

import com.frazzle.main.domain.game.roby.entity.Roby;
import com.frazzle.main.domain.game.roby.entity.RobyUser;

public interface RobyEventListener {
    void onUserAdded(Roby roby, RobyUser robyUser);
    void onUserRemoved(Roby roby, RobyUser robyUser);
}