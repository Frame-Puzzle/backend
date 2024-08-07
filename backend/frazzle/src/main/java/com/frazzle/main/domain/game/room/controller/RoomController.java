package com.frazzle.main.domain.game.room.controller;

import com.frazzle.main.domain.game.chat.dto.SendMessageDto;
import com.frazzle.main.domain.game.room.entity.Room;
import com.frazzle.main.domain.game.room.entity.RoomNotification;
import com.frazzle.main.domain.game.room.entity.RoomUser;
import com.frazzle.main.domain.game.room.service.RoomService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RoomService roomService;
    private final UserService userService;
    private final UserRepository userRepository;

    @MessageMapping("/room/entry/{boardId}")
    public void entryChat(
            @DestinationVariable int boardId,
            SendMessageDto sendMessageDto,
            SimpMessageHeaderAccessor accessor) {
        String email = (String) accessor.getSessionAttributes().get("senderEmail");

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        RoomUser roomUser = RoomUser.createRoomUser(user.getUserId(), user.getNickname(), user.getProfileImg());
        roomService.addUserToRoom(boardId, roomUser);

        Room room = roomService.getRoom(boardId);
        log.info("entryChat roomId={}, room={}", boardId, room);
        RoomNotification notification = RoomNotification.createRoomNotification("입장", room);

        simpMessagingTemplate.convertAndSend("/sub/room/" + boardId, notification);
    }

    @MessageMapping("/room/exit/{boardId}")
    public void exitChat(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @DestinationVariable int boardId,
            SendMessageDto sendMessageDto,
            StompHeaderAccessor headerAccessor) {

        log.info("exitChat roomId={}, sendMessageDto={}", boardId, sendMessageDto);
        log.info(sendMessageDto.toString());

        User user = userPrincipal.getUser();
        log.info(user.getNickname());

        RoomUser roomUser = RoomUser.createRoomUser(user.getUserId(), user.getNickname(), user.getProfileImg());
        roomService.removeUserFromRoom(boardId, roomUser);

        // Notify all users about the updated user list
        Room room = roomService.getRoom(boardId);
        RoomNotification notification = RoomNotification.createRoomNotification("퇴장", room);
        simpMessagingTemplate.convertAndSend("/sub/room/" + boardId, notification);
    }
}
