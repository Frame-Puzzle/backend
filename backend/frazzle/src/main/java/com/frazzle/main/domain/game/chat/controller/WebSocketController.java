package com.frazzle.main.domain.chat.controller;


import com.frazzle.main.domain.game.chat.dto.SendMessageDto;
import com.frazzle.main.domain.game.chat.service.ChatService;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    @MessageMapping("/message/entry/{boardId}")
    public void entryChat(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @DestinationVariable int boardId,
            SendMessageDto sendMessageDto,
            StompHeaderAccessor headerAccessor) {
        sendMessageDto = chatService.entryChat(userPrincipal, boardId, sendMessageDto);
        log.info("message: " + sendMessageDto.getMessage());
//        headerAccessor.getSessionAttributes().put("roomId", String.valueOf(boardId));  // 세션에 방 ID 저장
        log.info("chat"+userPrincipal.getId());
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId, sendMessageDto);
    }

    @MessageMapping("/message/{boardId}")
    public void sendMessage(
            @DestinationVariable int boardId,
            SendMessageDto sendMessage) {
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId, sendMessage);
    }

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
