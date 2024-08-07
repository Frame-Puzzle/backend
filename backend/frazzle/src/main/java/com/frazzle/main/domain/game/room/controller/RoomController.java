package com.frazzle.main.domain.game.room.controller;

import com.frazzle.main.domain.game.chat.dto.SendMessageDto;
import com.frazzle.main.domain.game.chat.service.ChatService;
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
    private final ChatService chatService;
    private final UserService userService;
    private final UserRepository userRepository;

    //대기방 입장
    @MessageMapping("/room/entry/{boardId}")
    public void entryChat(
            @DestinationVariable int boardId,
            SendMessageDto sendMessageDto,
            SimpMessageHeaderAccessor accessor) {

        //jwt로 부터 이메일을 얻어 유저를 얻음
        String email = (String) accessor.getSessionAttributes().get("senderEmail");

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //대기방에서 유저의 정보
        //유저 아이디, 유저 닉네임, 유저 프로필 필요
        RoomUser roomUser = RoomUser.createRoomUser(user.getUserId(), user.getNickname(), user.getProfileImg());

        //퍼즐판의 id를 통해 대기방을 만들거고 유저 추가
        roomService.addUserToRoom(boardId, roomUser);

        //보드id를 통해 대기방 정보 찾기
        Room room = roomService.getRoom(boardId);

        log.info("entryChat roomId={}, room={}", boardId, room);

        //알림 추가
        RoomNotification notification = RoomNotification.createRoomNotification("입장", room);

        // /sub/room으로 메시지 보내기
        simpMessagingTemplate.convertAndSend("/sub/room/" + boardId, notification);

        //채팅에 접속
        sendMessageDto = chatService.entryChat(roomUser, boardId, sendMessageDto);

        log.info("message: " + sendMessageDto.getMessage());

        log.info("user.getUserId: "+user.getUserId());

        // /sub/message를 구독한 메서드에 메시지 보냄
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId, sendMessageDto);

    }

    //대기방 나가기
    @MessageMapping("/room/exit/{boardId}")
    public void exitChat(
            @DestinationVariable int boardId,
            SendMessageDto sendMessageDto,
            SimpMessageHeaderAccessor accessor) {

        log.info("exitChat roomId={}, sendMessageDto={}", boardId, sendMessageDto);
        log.info(sendMessageDto.toString());

        //jwt로 부터 이메일을 얻어 유저를 얻음
        String email = (String) accessor.getSessionAttributes().get("senderEmail");

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        RoomUser roomUser = RoomUser.createRoomUser(user.getUserId(), user.getNickname(), user.getProfileImg());
        //유저 제거
        roomService.removeUserFromRoom(boardId, roomUser);

        //대기방에 메시지 보내기
        Room room = roomService.getRoom(boardId);
        RoomNotification notification = RoomNotification.createRoomNotification("퇴장", room);
        simpMessagingTemplate.convertAndSend("/sub/room/" + boardId, notification);
    }
}
