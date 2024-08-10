package com.frazzle.main.domain.socket.game.controller;

import com.frazzle.main.domain.socket.game.dto.StartRequestDto;
import com.frazzle.main.domain.socket.game.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/start/{boardId}")
    public void sendMessage(
            @DestinationVariable int boardId,
            StartRequestDto moveRequestDto,
            SimpMessageHeaderAccessor accessor) {
        simpMessagingTemplate.convertAndSend("/sub/start/" + boardId);
    }
}
