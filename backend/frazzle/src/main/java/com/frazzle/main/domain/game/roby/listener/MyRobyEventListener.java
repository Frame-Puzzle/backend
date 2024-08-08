package com.frazzle.main.domain.game.roby.listener;

import com.frazzle.main.domain.game.roby.entity.Roby;
import com.frazzle.main.domain.game.roby.entity.RobyNotification;
import com.frazzle.main.domain.game.roby.entity.RobyUser;
import com.frazzle.main.domain.game.roby.service.RobyService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyRobyEventListener implements RobyEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final RobyService robyService;

    @Override
    public void onUserAdded(Roby roby, RobyUser robyUser) {
        robyService.addUserToRoby(roby.getRobyId(), robyUser);
        RobyNotification robyNotification = RobyNotification.createRoomNotification("입장", roby);
        messagingTemplate.convertAndSend("/sub/room/" + roby.getRobyId(), roby);
    }

    @Override
    public void onUserRemoved(Roby roby, RobyUser robyUser) {
        robyService.removeUserFromRoby(roby.getRobyId(), robyUser);
        RobyNotification notification = RobyNotification.createRoomNotification("퇴쟝", roby);
        messagingTemplate.convertAndSend("/sub/room/" + roby.getRobyId(), notification);
    }

}
