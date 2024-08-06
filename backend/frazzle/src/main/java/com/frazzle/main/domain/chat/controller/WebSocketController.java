package com.frazzle.main.domain.chat.controller;

import com.frazzle.main.domain.chat.dto.SendMessageDto;
import com.frazzle.main.domain.chat.service.ChatService;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

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
            SendMessageDto sendMessageDto) {
        SendMessageDto sendMessage = chatService.entryChat(userPrincipal, boardId, sendMessageDto);
        log.info("message: " + sendMessage.getMessage());
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId,
                sendMessage);
    }

    @MessageMapping("/message/{boardId}")
    public void sendMessage(
            @DestinationVariable int boardId,
            SendMessageDto sendMessage) {
        log.info("message: " + sendMessage.getMessage());
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId,
                sendMessage);
    }

    @MessageMapping("/message/exit/{boardId}")
    public void exitChat(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @DestinationVariable int boardId,
            SendMessageDto sendMessage) {
        sendMessage.entryMessage(sendMessage.getNickname()+"님이 퇴장하셨습니다.");
        log.info("message: " + sendMessage.getMessage());
        simpMessagingTemplate.convertAndSend("/sub/message/" + boardId,
                sendMessage);
    }

}
