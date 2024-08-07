package com.frazzle.main.domain.chat.controller;


import com.frazzle.main.domain.game.chat.dto.SendMessageDto;
import com.frazzle.main.domain.game.chat.service.ChatService;
import com.frazzle.main.domain.game.room.entity.RoomUser;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import javax.annotation.Resource;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final UserRepository userRepository;

    @MessageMapping("/message/entry/{boardId}")
    public void entryChat(
            @DestinationVariable int boardId,
            SendMessageDto sendMessageDto,
            SimpMessageHeaderAccessor accessor) {

        //이메일로부터 사용자 찾기
        String email = (String) accessor.getSessionAttributes().get("senderEmail");
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        RoomUser roomUser = RoomUser.createRoomUser(user.getUserId(), user.getNickname(), user.getProfileImg());

        //채팅에 접속
        sendMessageDto = chatService.entryChat(roomUser, boardId, sendMessageDto);

        log.info("message: " + sendMessageDto.getMessage());

        log.info("user.getUserId: "+user.getUserId());

        // /sub/message를 구독한 메서드에 메시지 보냄
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId, sendMessageDto);
    }

    //채팅 보내는 메서드
    @MessageMapping("/message/{boardId}")
    public void sendMessage(
            @DestinationVariable int boardId,
            SendMessageDto sendMessage) {
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId, sendMessage);
    }

    //채팅 나가는 메서드
    @MessageMapping("/message/exit/{boardId}")
    public void exitChat(
            @Header("Authorization") String authorization,
            @DestinationVariable int boardId,
            SendMessageDto sendMessage) {
        sendMessage.entryMessage(sendMessage.getUserId() + "님이 퇴장하셨습니다.");
        log.info("message: " + sendMessage.getMessage());
        log.info(authorization);
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId, sendMessage);
    }
}
