package com.frazzle.main.domain.socket.game.controller;

import com.frazzle.main.domain.socket.game.dto.*;
import com.frazzle.main.domain.socket.game.service.GameService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
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

    @MessageMapping("/start/{boardId}")
    public void startGame(
            @DestinationVariable int boardId,
            StartRequestDto startRequestDto,
            SimpMessageHeaderAccessor accessor) {
        log.info("시작");
        gameService.startGame(startRequestDto);
    }

    // 퍼즐 완료했을 때 메서드
    @MessageMapping("/end/puzzle/{boardId}")
    public void endPuzzle(
            @DestinationVariable int boardId,
            EndRequestDto endRequestDto,
            SimpMessageHeaderAccessor accessor
    ) {
        // 이메일로부터 사용자 찾기
        String email = (String) accessor.getSessionAttributes().get("senderEmail");

        gameService.endPuzzle(boardId, email, endRequestDto.getTime());
        log.info("checkPuzzle processing completed for boardId={}", boardId);
    }

    // 퍼즐 나갔을 때 메서드
    @MessageMapping("/exit/puzzle/{boardId}")
    public void exitPuzzle(
            @DestinationVariable int boardId,
            SimpMessageHeaderAccessor accessor
    ) {
        // 이메일로부터 사용자 찾기
        String email = (String) accessor.getSessionAttributes().get("senderEmail");
        log.info("방 나가기");
        gameService.exitPuzzle(boardId, email);
    }

}
